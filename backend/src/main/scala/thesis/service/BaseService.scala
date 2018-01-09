package thesis.service

import akka.actor.{ActorRef, ActorSystem, Scheduler}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RequestContext}
import akka.stream.Materializer
import akka.util.Timeout
import io.circe.Json
import org.slf4j.LoggerFactory
import thesis.protocol.CommonErrorCode
import thesis.utils.CirceSupport

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
  * Created by liuziwei on 2017/5/5.
  */
trait BaseService extends CirceSupport{

  import io.circe.Error
  import io.circe.generic.auto._

  implicit val system: ActorSystem
  implicit val executor: ExecutionContextExecutor
  implicit val materializer: Materializer
  implicit val timeout: Timeout
  implicit val scheduler: Scheduler
  val masterService:ActorRef

//  val postPreProcessingActor:ActorRef[PostPreProcessingActor.Command]


  private val log = LoggerFactory.getLogger(this.getClass)


  def dealFutureResult(future: => Future[server.Route]) = {
    onComplete(future) {
      case Success(rst) => rst
      case Failure(e) =>
        e.printStackTrace()
        log.warn(s"internal error: ${e.getMessage}")
        complete(CommonErrorCode.InternalError(e.getMessage))
    }
  }

  def loggingAction: Directive[Tuple1[RequestContext]] = extractRequestContext.map{ ctx =>
    log.info(s"Access uri: ${ctx.request.uri} from ip ${ctx.request.uri.authority.host.address}.")
    ctx
  }

  def loggingGetIpAction  = extractClientIP.map{ ctx =>
    log.info(s"Access uri: from ip ${ctx.toIP.get.ip.toString}.")
    ctx
  }


}
