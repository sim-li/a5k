package controllers.pidparsing

import akka.actor.{Actor, ActorRef, Props}
import controllers.pidparsing.PidParser.TimeToUpdate
import de.bht.lischka.adminTool5k.ModelX._
import scala.concurrent.duration._
import sys.process._

object PidParser {
  def props(resultHandler: ActorRef) = Props(new PidParser(resultHandler))
  case object TimeToUpdate
}

class PidParser(resultHandler: ActorRef) extends Actor {
  import context._

  val r = scala.util.Random
  val macCommand = "top -l 1"
  val linuxCommand = "top -bn1"

  override def preStart = {
    context.system.scheduler.schedule(10 seconds, 10 seconds, self, TimeToUpdate)
  }

  override def receive: Receive = {
    case TimeToUpdate => PidParsingUtils(macCommand.!!).rows.map(systemStatsLine =>
      resultHandler ! ProcessUpdate(systemStatsLine))
  }

  def randomCpuStat = Some(Cpu(r.nextDouble()))

}