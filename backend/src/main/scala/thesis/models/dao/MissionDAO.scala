package thesis.models.dao

import thesis.utils.DBUtil._
import thesis.models.tables.SlickTables
import thesis.Boot.executor
import slick.jdbc.PostgresProfile.api._
import thesis.common.Constants.MissionState
import thesis.shared.ptcl.APIProtocol.SubmitMissionReq
/**
  * Created by liuziwei on 2018/1/22.
  */

object MissionDAO {

  private val tMission = SlickTables.tMasterMission

  def insert(req:SubmitMissionReq) = db.run(
    tMission.returning(tMission.map(_.id)) += SlickTables.rMasterMission(-1l,req.dataSetId,req.modelId,System.currentTimeMillis(),0,"","",req.fieldId)
  ).mapTo[Long]

  def change2RunState(node:String,id:Long) = db.run(
    tMission.filter(_.id === id).map(i => (i.nodeName,i.status)).update((node,MissionState.Run))
  )

  def change2FailState(id:Long) = db.run(
    tMission.filter(_.id === id).map(_.status).update(MissionState.Fail)
  )
}
