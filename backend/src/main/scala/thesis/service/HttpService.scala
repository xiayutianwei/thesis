package thesis.service

import akka.http.scaladsl.server.Directives._
/**
  * Created by liuziwei on 2018/1/3.
  */
trait HttpService extends NodeService with APIService{

  val routes = pathPrefix("master"){
    nodeRoutes ~ apiRoute
  }
}
