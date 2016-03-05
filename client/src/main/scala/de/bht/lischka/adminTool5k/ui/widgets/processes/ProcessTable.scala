package de.bht.lischka.adminTool5k.ui.widgets.processes

import java.util.UUID
import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Pid, ShellCommand, SystemStatsUpdate, Process}
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => jQ}
import rx._
import scala.None
import scalatags.JsDom
import scalatags.JsDom.TypedTag
import scalatags.JsDom.all._
import org.scalajs.dom.{Element, document}

object ProcessTable {
  def props = Props(new ProcessTable())
}

class ProcessTable() extends Actor {
  var statsEntries: Map[Pid, ActorRef] = Map()

  // @TODO: Move out or rename to ProcessSection
  val processSection = div(cls:="col-md-4")(
    div(cls:="panel panel-default")(
      div(cls:="panel panel heading")(
        b("Top System Stats"),
        tableRoot
      )
    )
  )

  val tableRootId = "processTable"
  val tableRoot: JsDom.TypedTag[dom.Element] = table(id:=tableRootId)(
    tr(
      th("PID"),
      th("COMMAND"),
      th("CPU"),
      th("TIME"),
      th("MEM")
    )
  )

  override def preStart = {
    Option(document.getElementById("process_section_target")) match {
      case Some(processSectionTarget) =>
        processSectionTarget.appendChild(processSection.render)
      case _ => println("Couldn't find process_section_target")
    }

  }

  def inactive: Receive = {
    case _ =>
  }

  def active: Receive = {
    case SystemStatsUpdate(statsLine: Process) =>
      statsLine.pid.foreach(processId => statsEntries.get(processId) match {
        case Some(statsEntry: ActorRef) => statsEntry ! SystemStatsUpdate(statsLine)
        case None =>
          val processRow = context.actorOf(ProcessRow.props(tableRootId))
          processRow ! statsLine
          statsEntries += processId -> processRow
      }
      )
    case _ => println("Default case")
  }

  def receive: Receive = inactive

}
