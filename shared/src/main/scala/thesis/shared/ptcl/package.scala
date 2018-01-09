package thesis.shared


/**
  * Created by liuziwei on 2017/10/16.
  */
package object shared {
  trait CommonRsp {
    val errCode: Int
    val msg: String
  }

  final case class ErrorRsp(
                             errCode: Int,
                             msg: String
                           ) extends CommonRsp

  final case class SuccessRsp(
                               errCode: Int = 0,
                               msg: String = "ok"
                             ) extends CommonRsp

  final case class ComRsp(
                           errCode: Int,
                           msg: String
                         )extends CommonRsp
}

