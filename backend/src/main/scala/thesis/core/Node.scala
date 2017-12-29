package thesis.core

import akka.actor.Actor.Receive
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.{ActorAttributes, OverflowStrategy, Supervision}
import akka.stream.scaladsl._
import akka.actor._
import akka.stream.Supervision.Decider
import org.slf4j._
import io.circe.generic.auto._
import io.circe.syntax._
import thesis.core.Mission.Instruct
/**
  * Created by liuziwei on 2017/12/26.
  */
object Node{
  sealed trait Command extends Heart.Command
  case class Register(name:String) extends Command
  case class RegisterRst(flow:Flow[Message, Message, Any])
  def props(name:String) = Props[Node](new Node(name))
  private def playInSink(actor: ActorRef) = Sink.actorRef[Heart.Command](actor, "stop")
  def getFlow(heart: ActorRef,mission:ActorRef): Flow[String, Instruct, Any] = {
    val in =
      Flow[String]
        .map { s =>
          Heart.HeartBeet
        }
        .to(playInSink(heart))

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

class Node(name:String) extends Actor {

  import Node._


  private val log = LoggerFactory.getLogger(this.getClass)
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
          context.actorOf(Heart.props)
        case Symbol.Mission =>
          context.actorOf(Mission.props)
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
        val flow: Flow[Message, Message, Any] =
          Flow[Message]
            .collect {
              case TextMessage.Strict(m) =>
                log.debug(s"msg from webSocket: $m")
                m
            }
            .via(getFlow(getChild(Symbol.Heart),getChild(Symbol.Mission)))
            .map {
              rsp =>
                log.debug(s"reply is $rsp \n ${rsp.asJson.noSpaces}")
                TextMessage.Strict(rsp.asJson.noSpaces)
            }.withAttributes(ActorAttributes.supervisionStrategy(decider))


        sender() ! RegisterRst(flow)
  }
}
