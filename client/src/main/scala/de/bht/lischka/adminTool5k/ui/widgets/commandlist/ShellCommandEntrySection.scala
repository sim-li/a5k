package de.bht.lischka.adminTool5k.ui.widgets.commandlist

import java.util.UUID

import akka.actor.{ActorRef, Props, Actor}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.{User, ShellCommand, CommandResult, ExecuteCommand}
import de.bht.lischka.adminTool5k.ui.widgets.commandlist.ShellCommandEntry.ScrollToEntry
import de.bht.lischka.adminTool5k.ui.widgets.commandlist.ShellCommandEntrySection.{SetUser, SendButtonClick}
import org.scalajs.jquery.{jQuery => jQ, _}

object ShellCommandEntrySection {
  def props = Props(new ShellCommandEntrySection)
  case class SendButtonClick(command: String)
  case class SetUser(user: User)
}

class ShellCommandEntrySection extends Actor {
  var commandEntries: Map[UUID, ActorRef] = Map()
  var user: User = new User(None)

  @Override
  override def preStart: Unit = {
    registerCallback()
  }

  def registerCallback() = {
    val sendCommandButton = jQ("#send_command_button")
     sendCommandButton click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        self ! SendButtonClick(commandTextfield.value().toString())
    }
    jQ("#command_textfield").keyup((eventData: JQueryEventObject) => {
      if (eventData.which == 13) {
        sendCommandButton click
      }
    })
  }

  override def receive: Receive = {
    case SetUser(u) => user = u

    case ExecuteCommand(alreadyIssuedCommand) => val id = alreadyIssuedCommand.issueInfo.id
      if (!commandEntries.contains(id)) {
        addShellCommandEntry(alreadyIssuedCommand)
      }

    case CommandResult(commandResult) => {
      val id = commandResult.issueInfo.id
      if (commandEntries.contains(id))
        commandEntries(id) ! ShellCommandEntry.UpdateEntry(commandResult)
      else
        addShellCommandEntry(commandResult)
    }

    case SendButtonClick(cmd: String) => {
      val commandToBeIssued = ShellCommand(user, cmd)
      context.parent ! SendMessage(ExecuteCommand(commandToBeIssued))
      addShellCommandEntry(commandToBeIssued)
      commandEntries(commandToBeIssued.issueInfo.id) ! ScrollToEntry
    }
  }

  def addShellCommandEntry(issuedCommand: ShellCommand): Unit = {
    commandEntries += issuedCommand.issueInfo.id -> context.actorOf(ShellCommandEntry.props(issuedCommand))
  }
}
