package thesis.utils

/**
  * Created by liuziwei on 2018/1/19.
  */

import akka.dispatch.PriorityGenerator
import akka.dispatch.UnboundedStablePriorityMailbox
import com.typesafe.config.Config
import akka.actor.ActorSystem
import thesis.core.Master.ReleaseMission

// We inherit, in this case, from UnboundedStablePriorityMailbox
// and seed it with the priority generator
class MyPrioMailbox(settings: ActorSystem.Settings, config: Config)
  extends UnboundedStablePriorityMailbox(
    // Create a new PriorityGenerator, lower prio means more important
    PriorityGenerator {
      // 'highpriority messages should be treated first if possible
      case ReleaseMission ⇒ 0


      // We default to 1, which is in between high and low
      case otherwise     ⇒ 1
    })
