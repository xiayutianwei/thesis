package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import org.slf4j.LoggerFactory
import thesis.core.Node.{GetNodeAllInfo, NodeAllInfoRst, RunMission}
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
import akka.pattern.ask
import thesis.Boot.executor
import akka.util.Timeout
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
/**
  * Created by liuziwei on 2017/12/26.
  */
object Master {
  def props = Props[Master](new Master)

  sealed trait Command
  case class SwitchState(state: Receive) extends Command
  case class SubmitMission(id:Long,req: SubmitMissionReq) extends Command
  case class MissionDone(id:Long) extends Command
  case class ReleaseMission(req:SubmitMission) extends Command
}

class Master extends Actor with Stash{

  import Master._
  import Node.Register

  implicit val timeout = Timeout(5.second)

  private val log = LoggerFactory.getLogger(this.getClass)

  private val waitMission = new mutable.Queue[SubmitMission]()
  private val runMission = new mutable.HashMap[String,(Long,Long,Long)]() // node => missionId startTime predictTime
  def getChild(name:String) = {
    context.child(name).getOrElse{
      val child = context.actorOf(Node.props(name),name)
      context.watch(child)
      child
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = super.preStart()

  override def receive: Receive = idle

  def idle:Receive = {
    case r@Register(name) =>
      getChild(name).forward(r)

    case r@SubmitMission(id,req) =>
      if(waitMission.nonEmpty){
        waitMission.enqueue(r)
      }else{
        process(req).onComplete{
          case Success(nodeOpt) =>
            if(nodeOpt.isDefined) getChild(nodeOpt.get) ! RunMission(id,req)
            else waitMission.enqueue(r)
            self ! SwitchState(idle)
          case Failure(e) =>
            log.error(s"mission id $id choose node error",e)
            context.system.scheduler.scheduleOnce(5 minute,self,r)
            self ! SwitchState(idle)
        }
        context.become(work)
      }

    case r@ReleaseMission(req) =>
      process(req.req).onComplete{
        case Success(nodeOpt) =>
          if(nodeOpt.isDefined) getChild(nodeOpt.get) ! RunMission(req.id,req.req)
          else {
            log.error("release mission didn't find free node")
            context.system.scheduler.scheduleOnce(5 minute,self,r)
          }
          self ! SwitchState(idle)
        case Failure(e) =>
          log.error(s"mission id ${req.id} choose node error",e)
          context.system.scheduler.scheduleOnce(5 minute,self,r)
          self ! SwitchState(idle)
      }
      context.become(work)

    case MissionDone(id) =>
      log.info(s"mission id $id done")
      if(waitMission.nonEmpty){
        val m = waitMission.dequeue()
        self ! ReleaseMission(m)
      }

    case Terminated(child) =>
      log.error(s"child ${child.path} terminated ...")
  }

  def work:Receive = {
    case SwitchState(state) =>
      unstashAll()
      context.become(state)

    case _ =>
      stash()

  }

  def process(req:SubmitMissionReq) = {
    val nodeInfoF = context.children.toList.map{child =>
      (child ? GetNodeAllInfo).map{
        case NodeAllInfoRst(node,state,cache) =>
          (node,state,cache.contains(req.dataSetId))
        case _ =>
          ("",false,false)
      }.recover{
        case e:Exception =>
          log.error(s"ask node all info from ${child.path} error",e)
          ("",false,false)
      }
    }
    Future.sequence(nodeInfoF).map{infos =>
      val exist = infos.find(i => i._2 && i._3)
      val onlineNode = infos.filter(_._2)
      if(exist.isDefined && !runMission.contains(exist.get._1)) Some(exist.get._1)
      else if(exist.isDefined){
        Some(exist.get._1)
      }else{
        val runKeys = runMission.keySet
        val freeNode = onlineNode.find(i => runKeys.contains(i._1))
        if(freeNode.isDefined) Some(freeNode.get._1)
        else None
      }
    }
  }
}
