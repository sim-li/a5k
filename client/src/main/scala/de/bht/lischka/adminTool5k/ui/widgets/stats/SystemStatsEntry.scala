package de.bht.lischka.adminTool5k.ui.widgets.stats

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.SystemStatsLine
import org.scalajs.jquery.{jQuery => jQ}
import rx._

object SystemStatsEntry {
  def props(systemStats: SystemStatsLine) = Props(new SystemStatsEntry(systemStats))
  case class UpdateEntry(systemStats: SystemStatsLine)
}

class SystemStatsEntry(systemStats: SystemStatsLine) extends Actor {
  import SystemStatsEntry._

  val view  = SystemStatsEntryView(Var(systemStats))

  override def preStart = {
    jQ("#stats_entry").append(view.render)
  }

  def receive: Receive = {
    case UpdateEntry(systemStatsUpdate: SystemStatsLine) =>
      if(systemStats.pid == systemStatsUpdate.pid) {
        view.stats() = systemStatsUpdate
      }

    case _ => println("Hit default case in view")
  }
}