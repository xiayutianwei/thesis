package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import org.slf4j.LoggerFactory
import thesis.core.Node.{GetNodeAllInfo, NodeAllInfoRst}
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
import akka.pattern.ask
import thesis.Boot.executor
import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Master {
  def props = Props[Master](new Master)

  sealed trait Command
  case class SwitchState(state: Receive) extends Command
  case class SubmitMission(req: SubmitMissionReq) extends Command
}

class Master extends Actor with Stash{

  import Master._
  import Node.Register

  implicit val timeout = 5.second

  private val log = LoggerFactory.getLogger(this.getClass)

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

    case SubmitMission(req) =>
      context.become(work)

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
      if(exist.isDefined && !runMission.contains(exist.get._1)) exist.get._1
      else if(exist.isDefined){
        exist.get._1
      }else{
        val freeNode = onlineNode
        runMission.keys
      }
    }
  }
}
