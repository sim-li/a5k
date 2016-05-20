package de.bht.lischka.adminTool5k.ui.widgets.stats

import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Pid, ShellCommand, SystemStatsUpdateRaw, SystemStatsEntry}
import de.bht.lischka.adminTool5k.ui.widgets.stats.SystemStatsSection.SystemStatsUpdate
import org.scalajs.jquery.{jQuery => jQ}

object SystemStatsSection {
  def props = Props(new SystemStatsSection())
  case class SystemStatsUpdate(allEntries: List[SystemStatsEntry])
}

class SystemStatsSection() extends Actor {
  def receive: Receive = {
    case SystemStatsUpdate(allEntries: List[SystemStatsEntry]) =>
      jQ("#stats_entry").empty()
      jQ("#stats_entry").append(SystemStatsEntryView.renderTitleColumns)
      val entriesOrderedByCpuUsage = allEntries.sortBy(_.cpu.map(c => c.usage)).reverse
      entriesOrderedByCpuUsage.foreach(entry => jQ("#stats_entry").append(SystemStatsEntryView.renderStatLine(entry)))

    case _ => println("Unhandled default case")
  }
}
1