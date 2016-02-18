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

  def RxGetOrElseEmpty[T](stat: Option[T]) = Rx {
    val noDataJet = ""
    stat.getOrElse(noDataJet).toString()
  }

  val processName: Rx[String] = RxGetOrElseEmpty[String] { stats().processName }

  val pid: Rx[String] = Rx { stats().pid.toString }

  val cpu: Rx[String] = RxGetOrElseEmpty[Stat] { stats().cpu }

  val time: Rx[String] = RxGetOrElseEmpty[Stat] { stats().time }

  val memoryUsage: Rx[String] = RxGetOrElseEmpty[Stat] { stats().memoryUsage }

  val systemStatsSection = tr(
    td(pid),
    // Expect js dom modifier. What's that, actually?
    td(processName),
    td(cpu),
    td(time),
    td(memoryUsage)
  )

  def render = systemStatsSection.render
}