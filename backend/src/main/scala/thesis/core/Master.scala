package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import org.slf4j.LoggerFactory
/**
  * Created by liuziwei on 2017/12/26.
  */
object Master {
  def props = Props[Master](new Master)
}

class Master extends Actor {

  import Node._

  private val log = LoggerFactory.getLogger(this.getClass)
  def getChild(name:String) = {
    context.child(name).getOrElse{
      val child = context.actorOf(Node.props(name))
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
    case Terminated(child) =>
      log.error(s"child ${child.path} terminated ...")
  }
}
