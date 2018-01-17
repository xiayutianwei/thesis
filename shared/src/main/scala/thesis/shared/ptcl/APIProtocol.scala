package thesis.shared.ptcl

import thesis.shared.shared.CommonRsp

/**
  * Created by liuziwei on 2017/10/27.
  */
object APIProtocol {

  case class SubmitMissionReq(dataSetId:Long,modelId:Long,fieldId:Long)
  case class SubmitMissionRsp(submitRst:Int,errCode:Int=0,msg:String="ok") extends CommonRsp

}
