package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.Session.GetUser
import de.bht.lischka.adminTool5k.view.{ShellCommandEntry, ShellCommandEntryView}
import org.scalajs.jquery.{jQuery => jQ, _}
import java.util.UUID
import rx._
import scala.collection.script.Update
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.rx.all._

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))
  case class HandleCommandExecution(command: String)
}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
  import MainScreen._

  var commandEntries: Map[UUID, ActorRef] = Map()

  override def receive: Receive = loggedOut

  override def preStart: Unit = {
    registerCallback()
    router ! RegisterListener(self)
  }

  def registerCallback() = {
    jQ("#send_command_button") click {
      (event: JQueryEventObject) =>
        val commandTextfield = jQ("#command_textfield")
        self ! HandleCommandExecution(commandTextfield.value().toString())
    }
  }

  def loggedOut: Receive = {
    case LoginUser(user: User) =>
      context.become(loggedIn(user))
      jQ("#main_container").show()
  }

  def loggedIn(user: User): Receive = {


    case HandleCommandExecution(cmd: String) =>
      val issuedCommand = ShellCommand(user, cmd)
      context.parent ! SendMessage(ExecuteCommand(issuedCommand))
      addShellCommandEntry(issuedCommand)

    case CommandResult(shellCommand) => {
      val id = shellCommand.issueInfo.id
      if (commandEntries.contains(id))
        commandEntries(id) ! ShellCommandEntry.UpdateEntry(shellCommand)
      else
        addShellCommandEntry(shellCommand)
    }

    case messageOut: SendMessage => router ! messageOut

    case _ => println("Mainscreen hit default case")
  }

  def addShellCommandEntry(issuedCommand: ShellCommand): Unit = {
    commandEntries += issuedCommand.issueInfo.id -> context.actorOf(ShellCommandEntry.props(issuedCommand))
  }

}
