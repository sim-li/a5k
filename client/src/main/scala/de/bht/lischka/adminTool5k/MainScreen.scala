package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.Session.GetUser
import de.bht.lischka.adminTool5k.view.{SystemStatsSection, ShellCommandEntry, ShellCommandEntryView}
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID
import rx._
import scala.collection.script.Update
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.rx.all._
import SystemStats._

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))
  case class HandleCommandExecution(command: String)
}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
  import MainScreen._

  var commandEntries: Map[UUID, ActorRef] = Map()

  override def preStart: Unit = {
    registerCallback()
    router ! RegisterListener(self)
    context.actorOf(SystemStatsSection.props(SystemStatsSection(), "system-stats-section")
  }

  def registerCallback() = {
    jQ("#send_command_button") click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        self ! HandleCommandExecution(commandTextfield.value().toString())
    }
  }

  override def receive: Receive = loggedOut

  // View must handle incomming msgs when logged out for command replay
  def commonOps: Receive = {
    //@TODO ShowExecuteCommand?
    case ExecuteCommand(shellCommand) =>
      val id = shellCommand.issueInfo.id
      if (!commandEntries.contains(id)) {
        addShellCommandEntry(shellCommand)
      }

    case CommandResult(shellCommand) => {
      val id = shellCommand.issueInfo.id
      if (commandEntries.contains(id))
        commandEntries(id) ! ShellCommandEntry.UpdateEntry(shellCommand)
      else
        addShellCommandEntry(shellCommand)
    }
  }

  def loggedOut: Receive = commonOps orElse {
    case LoginUser(user: User) =>
      context.become(loggedIn(user))
      jQ("#main_container").show()
  }

  def loggedIn(user: User): Receive = commonOps orElse {
    case HandleCommandExecution(cmd: String) => {
      val issuedCommand = ShellCommand(user, cmd)
      context.parent ! SendMessage(ExecuteCommand(issuedCommand))
      addShellCommandEntry(issuedCommand)
    }

    case messageOut: SendMessage => router ! messageOut

    case x => println(s"Mainscreen hit default case ${x}")
  }

  // Adapt ActorBinTree example and have command entries
  // communicate with each other. Invent reason to make this necessary
  def addShellCommandEntry(issuedCommand: ShellCommand): Unit = {
    commandEntries += issuedCommand.issueInfo.id -> context.actorOf(ShellCommandEntry.props(issuedCommand))
  }

}
