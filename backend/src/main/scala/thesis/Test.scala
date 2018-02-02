package thesis

import java.io._

import slick.dbio.{FlatMapAction, SequenceAction}
import thesis.models.tables.SlickTables
import thesis.utils.DBUtil._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
/**
  * Created by liuziwei on 2017/11/3.
  */
object Test extends App{

    val file = new File("E:/workspace/test/textClassify/train-post-9.csv")
  val in = new BufferedInputStream(new FileInputStream(file))
  val ff = Source.fromFile(file).getLines().toList.map{s =>
      val aa = s.split("\\|#\\|")
      aa.map(a => "\""+a+"\"").mkString(",")
    }
  val out = new OutputStreamWriter(new FileOutputStream(new File("E:/workspace/test/textClassify/train.csv")))
  ff.foreach{f =>
    out.write(f)
    out.write("\r\n")
  }
  out.close()
}
object Test1 extends App{
  val cFile = new File("E:/研究生毕设/测试用模型/文本分类/board_classify_smth.txt")
  val file = new File("E:/研究生毕设/测试用模型/文本分类/eval.txt")
  val classify = Source.fromFile(cFile).getLines().toList.map(s => (s.split(" ")(0),s.split(" ")(1).toInt)).toMap
  val ff = Source.fromFile(file).getLines().toList.flatMap{s =>
    val aa = s.split("\\|#\\|")
    val c = classify.get(aa(8))
    if(c.isDefined && aa.length == 12) Some(aa.map(a => "\""+a+"\"").mkString(",")+","+c.get)
    else None
  }
  val out = new OutputStreamWriter(new FileOutputStream(new File("E:/workspace/test/textClassify/train.csv")))
  ff.foreach{f =>
    out.write(f)
    out.write("\r\n")
  }
  out.close()
}
