package de.bht.lischka.adminTool5k.ui.widgets.stats

import de.bht.lischka.adminTool5k.ModelX.{Stat, SystemStatsLine}
import rx._

import scalatags.JsDom.all._
import scalatags.rx.all._

object SystemStatsEntryView {
  def apply(stats: Var[SystemStatsLine]) = new SystemStatsEntryView(stats)
}

class SystemStatsEntryView(val stats: Var[SystemStatsLine]) {
  import rx._

  val processName: Rx[String] = Rx { stats().processName.toString() }

  val pid: Rx[String] = Rx { stats().pid.toString }

  val cpu: Rx[String] = Rx { stats().cpu.toString }

  val time: Rx[String] = Rx { stats().time.toString }

  val memoryUsage: Rx[String] = Rx { stats().memoryUsage.toString }

  val systemStatsSection = tr(
    td(pid),
    td(processName),
    td(cpu),
    td(time),
    td(memoryUsage)
  )

  def render = systemStatsSection.render
}
