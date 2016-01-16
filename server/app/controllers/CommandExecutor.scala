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

  def formatResponse(responseText: String) = {
      responseText.replace("\n", "<br>").replace(" ", "&nbsp;")
  }

  override def receive: Receive = {
    case ExecuteCommand(shellCommand) =>
    val cmd: String = shellCommand.command
    val cmdResponse = formatResponse(cmd.!!)
    val answer = shellCommand.copy(executionInfo = Some(ExecutionInfo(cmdResponse, new Date(), true)))
    resultReceiver ! SendMessage(answer)
  }
}
