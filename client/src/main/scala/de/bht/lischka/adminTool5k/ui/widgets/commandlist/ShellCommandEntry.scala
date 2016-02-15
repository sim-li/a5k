package de.bht.lischka.adminTool5k.ui.widgets.commandlist

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.ShellCommand
import org.scalajs.jquery.{jQuery => jQ}
import rx._

object ShellCommandEntry {
  def props(shellCommand: ShellCommand) = Props(new ShellCommandEntry(shellCommand))
  case class UpdateEntry(command: ShellCommand)
}

class ShellCommandEntry(shellCommand: ShellCommand) extends Actor {
  import ShellCommandEntry._

  val view  = ShellCommandEntryView(Var(shellCommand))

  override def preStart = {
    jQ("#command_list").append(view.render)
  }

  def receive: Receive = {
    case UpdateEntry(shellCommandUpdate: ShellCommand) =>
      if(shellCommand.issueInfo.id  == shellCommandUpdate.issueInfo.id) {
        view.shellCommand() = shellCommandUpdate
      }

    case _ => println("Hit default case in view")
  }
}