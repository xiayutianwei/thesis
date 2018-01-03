package thesis.core

import akka.actor.Actor.Receive
import akka.actor._
import org.slf4j._
/**
  * Created by liuziwei on 2017/12/26.
  */
object Heart {

  trait Command
  object HeartBeet extends Command
  case class Register(name:String) extends Command

  def props = Props[Heart](new Heart)



}

class Heart extends Actor {
  import Heart._

  private var lastTime = 0l

  private val log = LoggerFactory.getLogger(this.getClass)

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info(s"${context.self.path} starting...")
  }

  override def receive: Receive = idle

  def idle:Receive =  {
    case HeartBeet =>
      log.info(s"${self.path} receive heartbeat")
      lastTime = System.currentTimeMillis()
    case Register(name) =>
      log.info(s"node $name register success")
  }
}
