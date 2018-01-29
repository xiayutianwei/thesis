package thesis.service

import akka.http.scaladsl.server.Directives._
import org.slf4j.LoggerFactory
import thesis.core.Master.{MissionInfo, SubmitMission}
import thesis.models.dao.MissionDAO
import thesis.protocol.CommonErrorCode
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
import thesis.shared.shared.{ErrorRsp, SuccessRsp}

import scala.concurrent.Future
/**
  * Created by liuziwei on 2018/1/17.
  */
object APIProtocol{
  def SubmitMissionError(msg:String) = ErrorRsp(10010001,msg)
}
trait APIService extends BaseService{

  import io.circe.generic.auto._
  import io.circe._
  import APIProtocol._

  private val log = LoggerFactory.getLogger(this.getClass)

  def submitMission = (path("submit" / "mission") & post){
    entity(as[Either[Error,SubmitMissionReq]]){
      case Right(req) =>
        dealFutureResult{
          MissionDAO.insert(req).flatMap{id =>
            if(id > 0){
              MissionDAO.getMissionInfo(req).map {info =>
                if(info._1.isDefined && info._2.isDefined && info._3.isDefined) {
                  masterService ! SubmitMission(id, MissionInfo(info._1.get,info._2.get,info._3.get))
                  complete(SuccessRsp())
                }else{
                  log.error("cat not get mission info",req)
                  complete(SubmitMissionError("can not get mission info"))
                }
              }
            }else{
              log.error("insert mission to db fail")
              Future.successful(complete(SubmitMissionError("insert mission fail")))
            }
          }.recover{
            case e:Exception =>
              log.error("insert mission to db error",e)
              complete(SubmitMissionError("insert mission error"))
          }
        }
      case Left(e) =>
        complete(CommonErrorCode.ParseJsonError)
    }
  }

  val apiRoute = pathPrefix("api"){
    submitMission
  }
}
