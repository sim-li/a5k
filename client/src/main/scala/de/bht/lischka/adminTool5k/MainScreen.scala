package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.Session.GetUser
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))
  case class AddCommandToList(command: ShellCommand)
  case class HandleCommandExecution(command: String)
}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
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

    case shellCommand: ShellCommand => updateWithExecutionInfo(shellCommand)

    case other: Any => router ! other
  }

  def updateWithExecutionInfo(shellCommand: ShellCommand) = {
    // Throws this error:
    // Syntax error, unrecognized expression: ${shellCommand.issueInfo.commandId}-command-response
    println("Gen-ning ids")
    def id = shellCommand.issueInfo.commandId.toString()
    val responseFieldId: String = "#" + id  + "-command-response"
    val executedTimestampId: String = "#" + id + "-command-executed"
    println("After id gen")

    shellCommand.executionInfo match {
      case Some(info: ExecutionInfo) =>
        val response: String = info.response
        val executionTime: String = info.commandExecuted.toString()
        val responseField = jQ(responseFieldId)
        val executedTimestampField = jQ(executedTimestampId)
        println(s"Response field is ${responseField}")
        println(s"Executed time stamp field is ${executedTimestampField}")
        responseField.text(response)
        executedTimestampField.text("Executed at "+ executionTime.toString())
      case None =>
        jQ(responseFieldId).text("")
        jQ(executedTimestampId).text("Failure executing command")
    }
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
