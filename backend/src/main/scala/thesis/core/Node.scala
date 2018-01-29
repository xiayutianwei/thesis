package thesis.core

import akka.Done
import akka.actor.Actor.Receive
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.{ActorAttributes, OverflowStrategy, Supervision}
import akka.stream.scaladsl._
import akka.actor._
import akka.stream.Supervision.Decider
import org.slf4j._
import thesis.Boot.materializer
import thesis.core.Master.MissionDone
import thesis.core.Mission.{AllotMissionFail, Instruct}
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
import thesis.shared.shared.ErrorRsp
import thesis.utils.CirceSupport
/**
  * Created by liuziwei on 2017/12/26.
  */
object Node{
  private val log = LoggerFactory.getLogger(this.getClass)
  sealed trait Command extends Heart.Command
  case class Register(name:String) extends Command
  case object Disconnected extends Command
  case class RegisterRst(flow:Flow[Message, Message, Any])
  case object GetNodeState extends Command
  case object GetNodeDataCache extends Command
  case object GetNodeAllInfo extends Command
  case class NodeStateRst(node:String,state:Boolean)
  case class NodeDataCacheRst(node:String,cache:Set[Long])
  case class NodeAllInfoRst(node:String,state:Boolean,cache:Set[Long])
  case class RemoveDataSet(id:Long) extends Command
  case class AddDataSet(id:Long) extends Command
  def props(name:String) = Props[Node](new Node(name))
  private def playInSink(actor: ActorRef) = Sink.actorRef[Heart.Command](actor, Done)
  def getFlow(name:String,heart: ActorRef,mission:ActorRef): Flow[String, Instruct, Any] = {
    val tmpReceiver = thesis.Boot.system.actorOf(Props(new Actor {
      override def receive: Receive = {
          case "heart beat" => heart ! Heart.HeartBeet
          case "stop" =>
            log.info(s"$name tmpReceiver got stop, stop myself.")
            context.self ! Disconnected
            context.stop(self)
          case m:String => mission ! Mission.Feedback(m)
          case a =>
            log.info(s"$name tmpReceive got unknown msg $a, ignore it")
      }
    }))
    val in = Sink.actorRef[String](tmpReceiver,"stop")

    val out =
      Source.actorRef[Instruct](3, OverflowStrategy.dropHead)
        .mapMaterializedValue(outActor => mission ! Mission.Register(outActor))


    Flow.fromSinkAndSource(in, out)
  }

  object Symbol{
    val Heart = "heart"
    val Mission = "mission"
  }
}

class Node(name:String) extends Actor with CirceSupport{

  import Node._
  import Mission.AllotMission
  import io.circe.generic.auto._
  import io.circe.syntax._


  private val log = LoggerFactory.getLogger(this.getClass)
  private var state = false
  private val cache = new collection.mutable.HashSet[Long]()
  private val decider: Decider = {
    e: Throwable =>
      e.printStackTrace()
      println(s"WS stream failed with $e")
      Supervision.Resume
  }

  def getChild(symbol:String) = {
    context.child(symbol).getOrElse{
      symbol match{
        case Symbol.Heart =>
          context.actorOf(Heart.props,"heart")
        case Symbol.Mission =>
          context.actorOf(Mission.props(name),"mission")
      }
    }
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"node $name create...")
  }

  override def receive: Receive = idle

  def idle:Receive = {
      case Register(_) =>
        state = true
        val flow: Flow[Message, Message, Any] =
          Flow[Message]
            .collect {
              case TextMessage.Strict(m) =>
                log.debug(s"msg from webSocket: $m")
                m
            }
            .via(getFlow(name,getChild(Symbol.Heart),getChild(Symbol.Mission)))
            .map {
              rsp =>
                log.debug(s"reply is $rsp \n ${rsp.asJson.noSpaces}")
                TextMessage.Strict(rsp.asJson.noSpaces)
            }.withAttributes(ActorAttributes.supervisionStrategy(decider))


        sender() ! RegisterRst(flow)

      case r@AllotMission(id,req,1) =>
        log.info(s"allot mission $id on node $name")
        getChild(Symbol.Mission) ! r

      case r@AllotMissionFail(id) =>
        context.parent ! r

      case AddDataSet(id) =>
        cache.add(id)

      case RemoveDataSet(id) =>
        cache.remove(id)

      case r@MissionDone(_) =>
        context.parent ! r
      case Disconnected =>
        state = false

      case GetNodeAllInfo =>
        sender() ! NodeAllInfoRst(name,state,cache.toSet)
      case GetNodeDataCache =>
        sender() ! NodeDataCacheRst(name,cache.toSet)
      case GetNodeState =>
        sender() ! NodeStateRst(name,state)

  }
}
