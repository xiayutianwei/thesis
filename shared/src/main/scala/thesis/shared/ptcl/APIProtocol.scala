package thesis.shared.ptcl

import thesis.shared.shared.CommonRsp

/**
  * Created by liuziwei on 2017/10/27.
  */
object APIProtocol {

  case class QueryByUserReq(userId:String)
  case class UserScore(fieldId:Long,field:String,sort:Double)
  case class QueryByUserRsp (score:Option[Iterable[UserScore]],errCode:Int=0,msg:String="ok") extends CommonRsp

}
