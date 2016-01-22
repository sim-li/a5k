package de.bht.lischka.adminTool5k.view

import java.util.UUID
import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.ModelX.ShellCommand
import org.scalajs.jquery._
import rx._
import org.scalajs.jquery.{jQuery => jQ, _}

import scala.collection.script.Update

object ShellCommandEntry {
  def props(shellCommand: ShellCommand) = Props(new ShellCommandEntry(shellCommand))
  case class Update(command: ShellCommand)
}

class ShellCommandEntry(shellCommand: ShellCommand) extends Actor {
  import ShellCommandEntry._

  val view  = ShellCommandEntryView(Var(shellCommand))

  override def preStart = {
    jQ("#command_list").append(view.render)
  }

  def receive: Receive = {
    case Update(updatedShellCommand: ShellCommand) =>
      view.shellCommand() = updatedShellCommand
  }
}