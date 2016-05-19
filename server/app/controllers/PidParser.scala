package controllers

import akka.actor.{Actor, ActorRef, Props}
import controllers.PidParser.TimeToUpdate
import de.bht.lischka.adminTool5k.ModelX._

import scala.concurrent.duration._
import scala.sys.process._

object PidParser {
  def props(resultHandler: ActorRef) = Props(new PidParser(resultHandler))
  case object TimeToUpdate
}

class PidParser(resultHandler: ActorRef) extends Actor {
  import context._
  val macCommand = "top -l 1"
  val linuxCommand = "top -bn1"

  override def preStart = {
    context.system.scheduler.schedule(5 seconds, 5 seconds, self, TimeToUpdate)
  }

  override def receive: Receive = {
    case TimeToUpdate =>
      resultHandler ! SystemStatsUpdateRaw(macCommand.!!)
  }
}