package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.Session.GetUser
import de.bht.lischka.adminTool5k.view.{ShellCommandEntry}
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))
  case class AddCommandToList(command: ShellCommand)
  case class HandleCommandExecution(command: String)
}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
  import MainScreen._

  var commandEntries: List[ShellCommandEntry] = List()

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

    case shellCommand: ShellCommand => updateCommanEntryWithExecutionInfo(shellCommand)

    case other: Any => router ! other
  }

  def updateCommanEntryWithExecutionInfo(shellCommand: ShellCommand) = {
    def id = shellCommand.issueInfo.commandId.toString()
    val responseFieldId: String = "#" + id  + "-command-response"
    val executedTimestampId: String = "#" + id + "-command-executed"

    shellCommand.executionInfo match {
      case Some(info: ExecutionInfo) =>
        val response: String = info.response
        val executionTime: String = info.commandExecuted.toString()
        val responseField = jQ(responseFieldId)
        val executedTimestampField = jQ(executedTimestampId)
        responseField.html(response)
        executedTimestampField.text("Executed at "+ executionTime.toString())
      case None =>
        jQ(responseFieldId).text("")
        jQ(executedTimestampId).text("Failure executing command")
    }
  }

  def modifyCommand(shellCommand: ShellCommand) = {
    val cmd: ShellCommandEntry = searchById(shellCommand.issueInfo.commandId)
    shellCommand.executionInfo match {
      case Some(info: ExecutionInfo) =>
        cmd.commandResponse() = info.response
        cmd.commandStatus() = info.commandExecuted.toString()
      case None =>
        cmd.commandResponse() = "Execution Error"
        cmd.commandStatus() = "-"
    }
  }

  // Ouch!
  def searchById(id: UUID): ShellCommandEntry = commandEntries.filter(_.shellCommand.issueInfo.commandId == id)(0)

  def addCommand(shellCommand: ShellCommand) = {
    val newEntry = ShellCommandEntry(shellCommand)
    commandEntries = newEntry :: commandEntries
    jQ("#command_list").append(newEntry.render)
  }
}
