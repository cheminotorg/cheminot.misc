package org.cheminot.misc.scheduler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

object Scheduler {

  def schedule(delay: FiniteDuration, period: FiniteDuration)(f: ScheduledExecutor => Unit): ScheduledExecutor = {
    val executor = ScheduledExecutor(1)
    executor.delayExecution(f(executor))(period).onSuccess {
      case _ => executor.schedule(f(executor))(period)
    }
    executor
  }
}
