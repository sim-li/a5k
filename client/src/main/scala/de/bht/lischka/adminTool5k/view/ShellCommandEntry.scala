package de.bht.lischka.adminTool5k.view

import java.util.UUID
import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, User, IssueInfo, ShellCommand}
import org.scalajs.jquery._
import rx._
import org.scalajs.jquery.{jQuery => jQ, _}

import scala.collection.script.Update

object ShellCommandEntry {
  def props(command: String, user: User) = Props(new ShellCommandEntry(ShellCommand(command, IssueInfo(user))))
  case class UpdateEntry(command: ShellCommand)
}

class ShellCommandEntry(shellCommand: ShellCommand) extends Actor {
  import ShellCommandEntry._

  val view  = ShellCommandEntryView(Var(shellCommand))

  override def preStart = {
    jQ("#command_list").append(view.render)
    context.parent ! SendMessage(ExecuteCommand(shellCommand))
  }

  def receive: Receive = {
    case UpdateEntry(shellCommandUpdate: ShellCommand) =>
      if(shellCommand.issueInfo.id  == shellCommandUpdate.issueInfo.id) {
        view.shellCommand() = shellCommandUpdate
      }

    case _ => println("Hit default case in view")
  }
}