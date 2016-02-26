package de.bht.lischka.adminTool5k.ui.widgets.stats

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{SystemStatsUpdate, SystemStatsLine}
import org.scalajs.jquery.{jQuery => jQ}
import rx._

object SystemStatsEntry {
  def props(systemStats: SystemStatsLine) = Props(new SystemStatsEntry(systemStats))
}

class SystemStatsEntry(systemStats: SystemStatsLine) extends Actor {
  import SystemStatsEntry._

  val view  = SystemStatsEntryView(Var(systemStats))

  override def preStart = {
    jQ("#stats_entry").append(view.render)
  }

  def receive: Receive = {
    case SystemStatsUpdate(systemStatsUpdate: SystemStatsLine) =>
      if(systemStats.pid != systemStatsUpdate.pid) {
        throw new RuntimeException(s"Received UpdateEntry where PID ${systemStats.pid} doesn't match")
      }
      println(s"Got update ${systemStatsUpdate}")
      view.stats() = systemStatsUpdate

    case default => throw new RuntimeException(s"SystemStats entry got a message that doesn't interest him (${default})")
  }
}