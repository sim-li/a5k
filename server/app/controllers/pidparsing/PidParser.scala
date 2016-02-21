package controllers.pidparsing

import akka.actor.{Actor, ActorRef, Props}
import controllers.pidparsing.PidParser.TimeToUpdate
import de.bht.lischka.adminTool5k.ModelX._

import scala.concurrent.duration._

object PidParser {
  def props(resultHandler: ActorRef) = Props(new PidParser(resultHandler))
  case object TimeToUpdate
}

class PidParser(resultHandler: ActorRef) extends Actor {
  import context._

  val r = scala.util.Random

  override def preStart = {
    context.system.scheduler.schedule(1 seconds, 1 seconds, self, TimeToUpdate)
  }

  override def receive: Receive = {
    case TimeToUpdate =>
      resultHandler ! SystemStatsUpdate(SystemStatsLine(Pid(1), Some("La Macarena"), randomCpuStat))
      resultHandler ! SystemStatsUpdate(SystemStatsLine(Pid(2), Some("Los Vallcarca Surf"), randomCpuStat))
      resultHandler ! SystemStatsUpdate(SystemStatsLine(Pid(3), Some("Rangbang"), randomCpuStat))
  }

  def randomCpuStat = Some(Cpu(r.nextDouble()))

}