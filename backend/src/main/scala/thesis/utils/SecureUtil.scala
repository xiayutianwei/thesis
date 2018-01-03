package thesis.utils

import org.apache.commons.codec.digest.DigestUtils

import scala.util.Random

/**
  * User: Taoz
  * Date: 7/8/2015
  * Time: 8:42 PM
  */
object SecureUtil {

  val random = new Random(System.currentTimeMillis())

  val chars = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  )


  final case class PostEnvelope(
    appId: String,
    sn: String,
    timestamp: String,
    nonce: String,
    data: Option[String],
    signature: String
  )

  def checkPostEnvelope(envelope: PostEnvelope, secureKey: String): Boolean = {
    import envelope._
    val params = List(appId, sn, timestamp, nonce, data.getOrElse(""))
    checkSignature(params, signature, secureKey)
  }


  def genPostEnvelope(appId: String, sn: String, data: String, secureKey: String): PostEnvelope = {
    val params = List(appId, sn, data)
    val (timestamp, nonce, signature) = generateSignatureParameters(params, secureKey)
    PostEnvelope(appId, sn, timestamp, nonce, Some(data), signature)
  }


  def getSecurePassword(password: String, ip: String, timestamp: Long): String = {
    DigestUtils.sha1Hex(DigestUtils.md5Hex(timestamp + password) + ip + timestamp)
  }

  def checkSignature(parameters: List[String], signature: String, secureKey: String): Boolean = {
    generateSignature(parameters, secureKey) == signature
  }

  def generateSignature(parameters: List[String], secureKey: String): String = {
    val strSeq = (secureKey :: parameters).sorted.mkString("")
    //println(s"strSeq: $strSeq")
    DigestUtils.sha1Hex(strSeq)
  }

  def generateSignatureParameters(parameters: List[String], secureKey: String): (String, String, String) = {
    val timestamp = System.currentTimeMillis().toString
    val nonce = nonceStr(6)
    val pList = nonce :: timestamp :: parameters
    val signature = generateSignature(pList, secureKey)
    (timestamp, nonce, signature)
  }

  def nonceStr(length: Int): String = {
    val range = chars.length
    (0 until length).map { _ =>
      chars(random.nextInt(range))
    }.mkString("")
  }


  def checkStringSign(str: String, sign: String, secureKey: String): Boolean = {
    stringSign(str, secureKey) == sign
  }

  def stringSign(str: String, secureKey: String): String = {
    DigestUtils.sha1Hex(secureKey + str)
  }


  def main(args: Array[String]) {

    val a=generateSignature(List("apptydic001","PttKWiMANz","1500365591394","o4u0HcLs6bZyfp2FK93TwvEtkH7AcKY9","{\"behavior\":\"in\",\"date\":\"20170718\",\"unitId\":\"2100090\",\"userType\":\"all\"}"),

    "fkqAf23f2k190K2eid830LkdIur3842P")
    println(a)
  }


}
