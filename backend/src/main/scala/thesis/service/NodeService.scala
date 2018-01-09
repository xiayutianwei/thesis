package thesis.service

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, RequestContext}
import thesis.core.Node
import akka.pattern.ask
import thesis.core.Node.RegisterRst
import thesis.shared.shared.ErrorRsp
/**
  * Created by liuziwei on 2018/1/3.
  */
trait NodeService extends BaseService{
  import io.circe.generic.auto._

  def subscribe = path("subscribe"){
    parameter('name){name =>
        dealFutureResult {
          (masterService ? Node.Register(name)).map {
            case RegisterRst(flow) =>
              handleWebSocketMessages(flow)
            case _ =>
              complete(ErrorRsp(1000100, ""))
          }
        }

    }
  }

  val nodeRoutes = pathPrefix("node"){

  }
}
