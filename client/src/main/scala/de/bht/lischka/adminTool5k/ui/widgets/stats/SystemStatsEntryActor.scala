package de.bht.lischka.adminTool5k.ui.widgets.stats

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{SystemStatsUpdate, SystemStatsEntry}
import org.scalajs.jquery.{jQuery => jQ}
import rx._

object SystemStatsEntryActor {
  def props(systemStats: SystemStatsEntry) = Props(new SystemStatsEntryActor(systemStats))
}

class SystemStatsEntryActor(systemStats: SystemStatsEntry) extends Actor {
  import SystemStatsEntryActor._

  val view  = SystemStatsEntryView(Var(systemStats))

  override def preStart = {
    jQ("#stats_entry").append(view.render)
  }

  def receive: Receive = {
    case entry: SystemStatsEntry =>
      if(systemStats.pid != entry.pid) {
        println(s"Received UpdateEntry where PID ${systemStats.pid} doesn't match")
      } else {
        view.stats() = entry
      }

    case default => println(s"Unhandled message in SystemStatsEntry")
  }
}