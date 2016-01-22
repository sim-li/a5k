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

  var commandEntries: List[ActorRef] = List()

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

  def loggedOut: Receive = {
    case LoginUser(user: User) =>
      context.become(loggedIn(user))
      jQ("#main_container").show()
  }

  def loggedIn(user: User): Receive = {
    case HandleCommandExecution(command: String) =>
      commandEntries = context.actorOf(ShellCommandEntry.props(command, user)) :: commandEntries

    case shellCommand: ShellCommand => commandEntries.foreach(_ ! ShellCommandEntry.UpdateEntry(shellCommand))

    case messageOut: SendMessage => router ! messageOut

    case _ => println("Mainscreen hit default case")
  }

}
