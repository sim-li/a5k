package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID

object MainScreen {
  def props(router: ActorRef) = Props(new MainScreen(router))
  case class AddCommandToList(command: ShellCommand)
  case class HandleCommandExecution(shellCommand: ShellCommand)
}

class MainScreen(router: ActorRef) extends Actor {
  import MainScreen._

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
        val issueInfo = IssueInfo(User("Rolf"), generateUniqueId(), new Date())
        val shellCommand: ShellCommand = ShellCommand(command, issueInfo)
        self ! HandleCommandExecution(shellCommand)
    }
  }

  def generateUniqueId() = {
    UUID.randomUUID()
  }

  def commandExecutedInfo(shellCommand: ShellCommand): Unit = {
    shellCommand.executionInfo match {
      case Some(executionInfo) => executionInfo.commandExecuted
      case None => "Pending"
    }
  }


  def loggedOut: Receive = {
    case LoginUser(user: User) =>
      context.become(loggedIn)
      println("Main screen becomming logged in")
      jQ("#main_container").show()
  }

  def loggedIn: Receive = {
    case HandleCommandExecution(shellCommand: ShellCommand) =>
      router ! SendMessage(ExecuteCommand(shellCommand))
      self ! AddCommandToList(shellCommand)

    case AddCommandToList(shellCommand) => addCommand(shellCommand)

    case other: Any => router ! other
  }

  def addCommand(shellCommand: ShellCommand) = {
    jQ("#command_list").append(
      s"""
         <div id="${shellCommand.issueInfo.commandId}" class='list-group-item'>
              <h4 class='list-group-item-heading'>
                <span class='badge'>${shellCommand.issueInfo.user.name}</span>
                ${shellCommand.command}
              </h4>
           <p class='list-group-item-text'>
             <code>
                <h6 id="${shellCommand.issueInfo.commandId}-command-response">
                  Proccessing command...
                </h6>
             </code>
           </p>
           <h6 class='list-group-item-footer text-right'>
             <div id="${shellCommand.issueInfo.commandId}-command-issued">
                <b>${shellCommand.issueInfo.commandIssued}</b>
             </div>
             <div id="${shellCommand.issueInfo.commandId.toString()}-command-executed">
                <b>Execution pending...</b>
             </div>
           </h6>
         </div>
       """.stripMargin)
  }
}
