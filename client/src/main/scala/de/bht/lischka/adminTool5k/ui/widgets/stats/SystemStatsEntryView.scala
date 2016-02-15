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

  def RxGetOrElseEmpty(stat: Option[Stat]) = Rx {
    val noDataJet = ""
    stat.getOrElse(noDataJet).toString()
  }

  val processName: Rx[String] = Rx { stats().processName }

  val pid: Rx[String] = RxGetOrElseEmpty { stats().pid }

  val cpu: Rx[String] = RxGetOrElseEmpty { stats().cpu }

  val time: Rx[String] = RxGetOrElseEmpty { stats().time }

  val memoryUsage: Rx[String] = RxGetOrElseEmpty { stats().memoryUsage }

  val systemStatsSection = tr(
    td(pid),
    td(processName),
    td(cpu),
    td(time),
    td(memoryUsage)
  )

  def render = systemStatsSection.render
}