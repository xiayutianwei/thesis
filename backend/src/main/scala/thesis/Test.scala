package thesis

import java.io.{InputStreamReader, LineNumberReader}

/**
  * Created by liuziwei on 2017/11/3.
  */
object Test {

  def main( args:Array[String]) {
    try {
      val process = Runtime.getRuntime().exec("/home/liuziwei/ssh.sh")
      val ir = new InputStreamReader(process.getInputStream())
      val input = new LineNumberReader(ir)
      var line = input.readLine()
      while(line  != null) {
        System.out.println(line)
        line = input.readLine()
      }
      input.close()
      ir.close()
    } catch  {
      // TODO: handle exception
      case e:Exception =>
        e.printStackTrace()
    }
  }

}
