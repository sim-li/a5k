package de.bht.lischka.adminTool5k.ui.widgets.stats

import de.bht.lischka.adminTool5k.ModelX.{Pid, Stat, SystemStatsEntry}
import scalatags.JsDom.all._

object SystemStatsEntryView {
  def renderTitleColumns = tr(
    th("PID"),
    th("COMMAND"),
    th("CPU"),
    th("TIME")
  ).render

  def renderStatLine(systemStatsEntry: SystemStatsEntry) = tr(
    td(systemStatsEntry.pid.map(p => p.pid)),
    td(systemStatsEntry.processName.map(pr => pr.name)),
    td(systemStatsEntry.cpu.map(c => c.usage)),
    td(systemStatsEntry.time.map(t => t.duration.toString))
  ).render
}
