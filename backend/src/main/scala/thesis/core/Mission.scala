package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import akka.http.scaladsl.model.ws.TextMessage
import org.slf4j._
import thesis.models.dao.MissionDAO
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq

import scala.util.{Failure, Success}
import scala.collection.mutable
import scala.concurrent.duration._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Mission {
  case class Register(outActor:ActorRef)
  case class SwitchState(state:Receive)
  sealed trait Instruct
  case class Feedback(msg:String) extends Instruct
  case class RunMission(id:Long,req:SubmitMissionReq) extends Instruct
  case class AllotMission(id:Long,req:SubmitMissionReq,retryTimes:Int) extends Instruct
  case class StopMission(id:Long) extends Instruct //TODO 处理停止任务

  def props(name:String) = Props[Mission](new Mission(name))
}

class Mission(name:String) extends Actor with Stash{

  import Mission._

  private val log = LoggerFactory.getLogger(this.getClass)
  private val subMissionFinish = new mutable.HashMap[String,Boolean]()
  subMissionFinish.put("field",false)
  subMissionFinish.put("data",false)
  subMissionFinish.put("model",false)




  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"${context.self.path} starting...")
  }

  override def receive: Receive = idle

  def idle:Receive = {
    case Register(outer) =>
      log.info(s"${self.path} receive register msg, become work...")
      context.become(work(outer))
      context.watch(outer)
    case m =>
      log.debug(s"${self.path} receive msg $m when idle, ignore it")
  }

  def work(outer:ActorRef):Receive = {
    case AllotMission(id,req,times) =>
      if(times > 5){
        log.error("retry (start mission) over 5 times,stop it")
        MissionDAO.change2FailState(id).onComplete{
          case Success(res) => if(res<=0) log.error(s"node $name change mission $id state to fail fail")
          case Failure(e) => log.error(s"node $name change mission $id state to fail error",e)
        }
      }else {
        MissionDAO.change2RunState(name, id).onComplete {
          case Success(res) =>
            if (res > 0) self ! RunMission(id, req)
            else context.system.scheduler.scheduleOnce(5 second, self, AllotMission(id, req,times+1))
            self ! SwitchState(work(outer))
          case Failure(e) =>
            log.error(s"node $name change mission $id state to run error", e)
            context.system.scheduler.scheduleOnce(5 second, self, AllotMission(id, req,times+1))
            self ! SwitchState(work(outer))
        }
        context.become(busy)
      }

    case RunMission(id,req) =>
      outer ! TextMessage.Strict("")

    case Feedback(m) =>

    case Terminated(child) =>
      log.error(s"${child.path} dead...")
      context.become(idle)
  }

  def busy:Receive = {
    case SwitchState(state) =>
      unstashAll()
      context.become(state)

    case m =>
      stash()
  }

}