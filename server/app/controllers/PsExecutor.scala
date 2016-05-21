package controllers

import akka.actor.{Actor, ActorRef, Props}
import controllers.PsExecutor.TimeToUpdate
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.pidparsing.PidParsingUtils
import scala.concurrent.duration._
import scala.sys.process._

object PsExecutor {
  def props(resultHandler: ActorRef) = Props(new PsExecutor(resultHandler))
  case object TimeToUpdate
  val TOP_OUTPUT_FILE = "topOutput.txt"
}

class PsExecutor(resultHandler: ActorRef) extends Actor {
  import context._

  val GET_STATS = "ps axo %cpu,%mem,pid,time,command"

  override def preStart = {
    context.system.scheduler.schedule(1000 milliseconds, 1000 milliseconds, self, TimeToUpdate)
  }

  override def receive: Receive = {
    case TimeToUpdate =>
      val result = SystemStatsUpdateRaw(GET_STATS.!!)
      val debug = PidParsingUtils(result.rawStats).rows
      resultHandler ! result
  }
}