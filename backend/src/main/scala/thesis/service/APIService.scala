package thesis.service

import akka.http.scaladsl.server.Directives._
/**
  * Created by liuziwei on 2018/1/17.
  */
trait APIService extends BaseService{

  import io.circe.generic.auto._

  def submitMission = (path("submit" / "mission") & post){
    complete("")
  }

  val apiRoute = pathPrefix("api"){
    submitMission
  }
}
