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
      //@TODO: Simplify this, move to actor ShellCommandEntry constructor
      val id = generateUniqueId()
      val issueInfo = IssueInfo(user, id, new Date())
      val shellCommand: ShellCommand = ShellCommand(command, issueInfo)
      commandEntries += (id -> context.actorOf(ShellCommandEntry.props(shellCommand)))
      router ! SendMessage(ExecuteCommand(shellCommand))

    case shellCommand: ShellCommand =>
      val shellCommandEntry: ActorRef = commandEntries(shellCommand.issueInfo.commandId)
      shellCommandEntry ! ShellCommandEntry.UpdateEntry(shellCommand)

    case other: Any => router ! other
  }

}
