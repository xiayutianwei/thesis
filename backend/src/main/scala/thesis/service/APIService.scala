package thesis.service

import akka.http.scaladsl.server.Directives._
import thesis.protocol.CommonErrorCode
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
/**
  * Created by liuziwei on 2018/1/17.
  */
trait APIService extends BaseService{

  import io.circe.generic.auto._
  import io.circe._

  def submitMission = (path("submit" / "mission") & post){
    entity(as[Either[Error,SubmitMissionReq]]){
      case Right(req) =>
        complete("")
      case Left(e) =>
        complete(CommonErrorCode.ParseJsonError)
    }
  }

  val apiRoute = pathPrefix("api"){
    submitMission
  }
}
