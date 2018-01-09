package thesis

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.routing.RoundRobinPool
import akka.stream.ActorMaterializer
import akka.typed.scaladsl.adapter.PropsAdapter
import akka.util.Timeout
import thesis.service.HttpService
import common.AppSettings._
import thesis.core.Master

import scala.util.{Failure, Success}

/**
  * Created by liuziwei on 2018/1/3.
  */
object Boot extends HttpService{

  import com.neo.sk.leader.common.AppSettings._
  import concurrent.duration._


  override implicit val system = ActorSystem("leaderSystem", config)
  // the executor should not be the default dispatcher.
  override implicit val executor: MessageDispatcher =
  system.dispatchers.lookup("akka.actor.my-blocking-dispatcher")

  override implicit val materializer = ActorMaterializer()

  override implicit val scheduler = system.scheduler
  override implicit val timeout = Timeout(60 seconds) // for actor asks

  override val masterService = system.actorOf(Master.props)


  val log: LoggingAdapter = Logging(system, getClass)

  def main(args: Array[String]):Unit ={
    log.info("Starting.")
    //    if(args(0) == "service") {
    val binding = Http().bindAndHandle(routes, httpInterface, httpPort)
    binding.onComplete {
      case Success(b) ⇒
        val localAddress = b.localAddress
        //            val f = new File(AppSettings.tmpFilePath)
        //            if(!f.exists()) f.mkdirs()
        log.info(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
      case Failure(e) ⇒
        log.error(s"Binding failed with ${e.getMessage}")
        system.terminate()
        System.exit(-1)
    }
    //    }else{





    //    }
  }

}
