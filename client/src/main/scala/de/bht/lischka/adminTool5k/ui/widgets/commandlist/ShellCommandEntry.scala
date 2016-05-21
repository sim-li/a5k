package de.bht.lischka.adminTool5k.ui.widgets.commandlist

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.ShellCommand
import org.scalajs.dom.html
import org.scalajs.jquery.{jQuery => jQ, _}
import rx._

import scala.scalajs.js

object ShellCommandEntry {
  def props(shellCommand: ShellCommand) = Props(new ShellCommandEntry(shellCommand))
  case class UpdateEntry(command: ShellCommand)
  case object ScrollToEntry
}

class ShellCommandEntry(shellCommand: ShellCommand) extends Actor {
  import ShellCommandEntry._

  val view = ShellCommandEntryView(shellCommand)
  var renderedEntry: Option[html.Div] = None

  override def preStart = {
    renderedEntry = Some(view.renderContainer)
    renderedEntry.map(c => jQ("#command_list").append(c))
  }

  def receive: Receive = {
    case ScrollToEntry => renderedEntry.map(c => jQ("#html,body").animate(js.Dictionary("scrollTop" -> c.offsetTop)))

    case UpdateEntry(shellCommandUpdate: ShellCommand) =>
      val htmlSection = jQ(s"#${shellCommandUpdate.issueInfo.id}")
      htmlSection.empty
      val updatedView = ShellCommandEntryView(shellCommandUpdate)
      htmlSection.append(updatedView.renderContents)

    case _ => println("Hit default case in view")
  }
}