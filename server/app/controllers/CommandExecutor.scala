package controllers

import java.util.Date
import akka.actor.{ActorRef, Props, Actor}
import akka.actor.Actor.Receive
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX._
import scala.util.{Failure, Success, Try}
import sys.process._

object CommandExecutor {
  def props(resultHandler: ActorRef) = Props(new CommandExecutor(resultHandler))
}

class CommandExecutor(resultReceiver: ActorRef) extends Actor {
  override def receive: Receive = {
    case ExecuteCommand(shellCommand) =>
      val bashResult = shellCommand.command.!!
      val cmdResponse = shellCommand.copy(executionInfo = Some(ExecutionInfo(bashResult, new Date(), true)))
      resultReceiver ! CommandResult(cmdResponse)
  }
}
