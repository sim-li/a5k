package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{SendMessage, LoggedIn, LoggedOut}
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, ShellCommand, User, IssueInfo}
import de.bht.lischka.adminTool5k.Router.RegisterUiComponent
import org.scalajs.jquery.{jQuery => jQ, _}

object MainScreen {
  def props(router: ActorRef) = Props(new MainScreen(router))
}

class MainScreen(router: ActorRef) extends Actor {
  override def receive: Receive = loggedOut

  override def preStart: Unit = {
    registerCallback()
    router ! RegisterUiComponent(self)
  }

  def registerCallback() = {
    jQ("#send_command_button") click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        val command: String = commandTextfield.value().toString()
        val issueInfo = IssueInfo(User("Rolf"), new Date())
        val shellCommand: ShellCommand = ShellCommand(command, issueInfo)
        router ! SendMessage(ExecuteCommand(shellCommand))
    }
  }

  def loggedOut: Receive = {
    case LoggedIn(user: User) =>
      context.become(loggedIn)
      jQ("#main_container").show()
  }

  def loggedIn: Receive = {
    case other: Any => router ! other
  }
}
