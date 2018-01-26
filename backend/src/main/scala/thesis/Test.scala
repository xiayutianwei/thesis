package thesis

import java.io.{InputStreamReader, LineNumberReader}

import slick.dbio.{FlatMapAction, SequenceAction}
import thesis.models.tables.SlickTables
import thesis.utils.DBUtil._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by liuziwei on 2017/11/3.
  */
object Test {
  private val tMission = SlickTables.tMasterMission
  def main( args:Array[String]) {
    aa
  }

  def aa = {
    val query = for{
      a <- tMission.filter(_.id === 0l).map(_.status).result
      b <- tMission ++= (1 to 10).map(i => SlickTables.rMasterMission(-1l))
    }yield{
      a
    }
    val q1 = tMission ++= (1 to 10).map(i => SlickTables.rMasterMission(-1l))


    query.transactionally match{
      case SequenceAction(actions) =>
        println(actions.length)
      case FlatMapAction(_,_,_) =>
        println("flatmap ")
    }
  }

}
