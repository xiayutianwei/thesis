package thesis.models.tables
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tMasterDataSet.schema ++ tMasterMission.schema ++ tMasterModel.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tMasterDataSet
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param name Database column name SqlType(varchar), Length(255,true), Default()
    *  @param path Database column path SqlType(text), Default()
    *  @param size Database column size SqlType(float8), Default(0.0)
    *  @param amount Database column amount SqlType(int4), Default(0)
    *  @param `type` Database column type SqlType(int4), Default(0) */
  final case class rMasterDataSet(id: Long, name: String = "", path: String = "", size: Double = 0.0, amount: Int = 0, `type`: Int = 0)
  /** GetResult implicit for fetching rMasterDataSet objects using plain SQL queries */
  implicit def GetResultrMasterDataSet(implicit e0: GR[Long], e1: GR[String], e2: GR[Double], e3: GR[Int]): GR[rMasterDataSet] = GR{
    prs => import prs._
      rMasterDataSet.tupled((<<[Long], <<[String], <<[String], <<[Double], <<[Int], <<[Int]))
  }
  /** Table description of table master_data_set. Objects of this class serve as prototypes for rows in queries.
    *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class tMasterDataSet(_tableTag: Tag) extends profile.api.Table[rMasterDataSet](_tableTag, "master_data_set") {
    def * = (id, name, path, size, amount, `type`) <> (rMasterDataSet.tupled, rMasterDataSet.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(path), Rep.Some(size), Rep.Some(amount), Rep.Some(`type`)).shaped.<>({r=>import r._; _1.map(_=> rMasterDataSet.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true), Default() */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true), O.Default(""))
    /** Database column path SqlType(text), Default() */
    val path: Rep[String] = column[String]("path", O.Default(""))
    /** Database column size SqlType(float8), Default(0.0) */
    val size: Rep[Double] = column[Double]("size", O.Default(0.0))
    /** Database column amount SqlType(int4), Default(0) */
    val amount: Rep[Int] = column[Int]("amount", O.Default(0))
    /** Database column type SqlType(int4), Default(0)
      *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Int] = column[Int]("type", O.Default(0))
  }
  /** Collection-like TableQuery object for table tMasterDataSet */
  lazy val tMasterDataSet = new TableQuery(tag => new tMasterDataSet(tag))

  /** Entity class storing rows of table tMasterMission
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param dataId Database column data_id SqlType(int8), Default(0)
    *  @param modelId Database column model_id SqlType(int8), Default(0)
    *  @param timestamp Database column timestamp SqlType(int8), Default(0)
    *  @param status Database column status SqlType(int4), Default(0)
    *  @param evaluate Database column evaluate SqlType(text), Default()
    *  @param nodeName Database column node_name SqlType(varchar), Length(255,true), Default() */
  final case class rMasterMission(id: Long, dataId: Long = 0L, modelId: Long = 0L, timestamp: Long = 0L, status: Int = 0, evaluate: String = "", nodeName: String = "")
  /** GetResult implicit for fetching rMasterMission objects using plain SQL queries */
  implicit def GetResultrMasterMission(implicit e0: GR[Long], e1: GR[Int], e2: GR[String]): GR[rMasterMission] = GR{
    prs => import prs._
      rMasterMission.tupled((<<[Long], <<[Long], <<[Long], <<[Long], <<[Int], <<[String], <<[String]))
  }
  /** Table description of table master_mission. Objects of this class serve as prototypes for rows in queries. */
  class tMasterMission(_tableTag: Tag) extends profile.api.Table[rMasterMission](_tableTag, "master_mission") {
    def * = (id, dataId, modelId, timestamp, status, evaluate, nodeName) <> (rMasterMission.tupled, rMasterMission.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(dataId), Rep.Some(modelId), Rep.Some(timestamp), Rep.Some(status), Rep.Some(evaluate), Rep.Some(nodeName)).shaped.<>({r=>import r._; _1.map(_=> rMasterMission.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column data_id SqlType(int8), Default(0) */
    val dataId: Rep[Long] = column[Long]("data_id", O.Default(0L))
    /** Database column model_id SqlType(int8), Default(0) */
    val modelId: Rep[Long] = column[Long]("model_id", O.Default(0L))
    /** Database column timestamp SqlType(int8), Default(0) */
    val timestamp: Rep[Long] = column[Long]("timestamp", O.Default(0L))
    /** Database column status SqlType(int4), Default(0) */
    val status: Rep[Int] = column[Int]("status", O.Default(0))
    /** Database column evaluate SqlType(text), Default() */
    val evaluate: Rep[String] = column[String]("evaluate", O.Default(""))
    /** Database column node_name SqlType(varchar), Length(255,true), Default() */
    val nodeName: Rep[String] = column[String]("node_name", O.Length(255,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tMasterMission */
  lazy val tMasterMission = new TableQuery(tag => new tMasterMission(tag))

  /** Entity class storing rows of table tMasterModel
    *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
    *  @param path Database column path SqlType(text), Default()
    *  @param exeFile Database column exe_file SqlType(varchar), Length(255,true), Default() */
  final case class rMasterModel(id: Long, path: String = "", exeFile: String = "")
  /** GetResult implicit for fetching rMasterModel objects using plain SQL queries */
  implicit def GetResultrMasterModel(implicit e0: GR[Long], e1: GR[String]): GR[rMasterModel] = GR{
    prs => import prs._
      rMasterModel.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table master_model. Objects of this class serve as prototypes for rows in queries. */
  class tMasterModel(_tableTag: Tag) extends profile.api.Table[rMasterModel](_tableTag, "master_model") {
    def * = (id, path, exeFile) <> (rMasterModel.tupled, rMasterModel.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(path), Rep.Some(exeFile)).shaped.<>({r=>import r._; _1.map(_=> rMasterModel.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column path SqlType(text), Default() */
    val path: Rep[String] = column[String]("path", O.Default(""))
    /** Database column exe_file SqlType(varchar), Length(255,true), Default() */
    val exeFile: Rep[String] = column[String]("exe_file", O.Length(255,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tMasterModel */
  lazy val tMasterModel = new TableQuery(tag => new tMasterModel(tag))
}
