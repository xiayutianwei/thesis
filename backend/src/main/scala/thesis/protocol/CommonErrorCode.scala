package thesis.protocol

import thesis.shared.shared.ErrorRsp

/**
  * Created by liuziwei on 2018/1/9.
  */
object CommonErrorCode {

  def InternalError(msg:String) = ErrorRsp(100001,msg)
  def ParseJsonError = ErrorRsp(100002,"parse request json error")
}
