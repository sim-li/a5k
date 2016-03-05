package de.bht.lischka.adminTool5k.ui.widgets.processes

import java.util.UUID
import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Stat, SystemStatsUpdate, Process}
import org.scalajs.jquery.{jQuery => jQ}
import org.scalajs.dom
import org.scalajs.dom.{Element, document}
import scalatags.JsDom.all._

object ProcessRow {
  //@TODO: Type harsher, to table
  def props(tableRootId: String) = Props(new ProcessRow(tableRootId, UUID.randomUUID()))
}

class ProcessRow(tableRootId: String, uuid: UUID) extends Actor {
  //@TODO: PID to seq, then for (pid in <- xyz yield div ( )

  def buildRow(process: Process) = tr(id := uuid.toString)(buildRowContent(process)).render

  def buildRowContent(process: Process) = {
    Seq(
      process.pid.map(tableColumn _),
      process.name.map(tableColumn _),
      process.cpu.map(tableColumn _),
      process.time.map(tableColumn _),
      process.memoryUsage.map(tableColumn _)
    )
  }

  def tableColumn(stat: Stat) = td(stat.toString)

  def receive: Receive = {
    case process: Process =>

      val table = Option(document.getElementById(tableRootId))
      table match {
        case Some(tableRoot) =>
          Option(document.getElementById(uuid.toString)) match {
            case Some(el: Element) =>
              el.innerHTML = buildRowContent(process).render.toString

            case None =>
              val row = buildRow(process)
              tableRoot.appendChild(row)
          }
        case None => println("Could not find table root")
      }

    case _ => println("Default case")
  }

}
