package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.MainScreen.ShowScreen
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, ShellCommand, User, IssueInfo}
import org.scalajs.jquery.{jQuery => jQ, _}

object MainScreen {
  def props(userState: ActorRef, router: ActorRef) = Props(new MainScreen(userState, router))
  case object ShowScreen
}

class MainScreen(userState: ActorRef, router: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
  }

  def registerCallback() = {
    jQ("#send_command_button") click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        val command: String = commandTextfield.value().toString()
        val issueInfo = IssueInfo(User("Rolf"), new Date())
        val shellCommand: ShellCommand = ShellCommand(command, issueInfo)
        self ! ExecuteCommand(shellCommand)
    }
  }

  override def receive: Receive = {
    case ShowScreen => jQ("#main_container").show()
    case other: Any => router ! other
  }
}

