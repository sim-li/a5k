package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.Session.GetUser
import de.bht.lischka.adminTool5k.view.{ShellCommandEntry}
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID
import rx._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.rx.all._

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))
  case class AddCommandToList(command: ShellCommand)
  case class HandleCommandExecution(command: String)
}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
  import MainScreen._

  var commandEntries: Map[UUID, ShellCommandEntry] = Map()

  override def receive: Receive = loggedOut

  override def preStart: Unit = {
    registerCallback()
    router ! RegisterListener(self)
  }

  def registerCallback() = {
    jQ("#send_command_button") click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        val command: String = commandTextfield.value().toString()
        self ! HandleCommandExecution(command)
    }
  }

  def generateUniqueId() = {
    UUID.randomUUID()
  }

  def loggedOut: Receive = {
    case LoginUser(user: User) =>
      context.become(loggedIn(user))
      jQ("#main_container").show()
  }

  def loggedIn(user: User): Receive = {
    case HandleCommandExecution(command: String) =>
      val issueInfo = IssueInfo(user, generateUniqueId(), new Date())
      val shellCommand: ShellCommand = ShellCommand(command, issueInfo)
      router ! SendMessage(ExecuteCommand(shellCommand))
      self ! AddCommandToList(shellCommand)

    case AddCommandToList(shellCommand) => addCommand(shellCommand)

    case shellCommand: ShellCommand =>
      modifyExistingShellCommandWith(shellCommand)

    case other: Any => router ! other
  }

  def modifyExistingShellCommandWith(updatedShellCommand: ShellCommand) = {
    val id = updatedShellCommand.issueInfo.commandId
    commandEntries(id).shellCommand() = updatedShellCommand
  }

  def addCommand(shellCommand: ShellCommand) = {
    val newEntry = ShellCommandEntry(Var(shellCommand))
    val commandId: UUID = shellCommand.issueInfo.commandId
    // @TODO This should fail if ID exists
    commandEntries += (commandId -> newEntry)
    jQ("#command_list").append(newEntry.render)
  }
}
