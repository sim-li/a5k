package controllers

import java.util.Date

import akka.actor.{ActorRef, Props, Actor}
import akka.actor.Actor.Receive
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, ExecutionInfo}
import sys.process._

object CommandExecutor {
  def props(resultHandler: ActorRef) = Props(new CommandExecutor(resultHandler))
}

class CommandExecutor(resultReceiver: ActorRef) extends Actor {
  override def receive: Receive = {
    case ExecuteCommand(shellCommand) =>
    val cmd: String = shellCommand.command
    val cmdResponse = cmd.!!
    val answer = shellCommand.copy(executionInfo = Some(ExecutionInfo(cmdResponse, new Date(), true)))
    // @TODO: Remove this when done experimenting
    Thread.sleep(1500)
    resultReceiver ! SendMessage(answer)
  }
}
