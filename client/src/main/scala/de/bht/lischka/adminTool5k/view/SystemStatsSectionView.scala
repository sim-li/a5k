package de.bht.lischka.adminTool5k.view

import de.bht.lischka.adminTool5k.ModelX.{Stat, Pid, SystemStats, ShellCommand}
import org.scalacheck.Prop.True
import rx._
import BootstrapCSS._

import scalatags.JsDom.all._

object SystemStatsView {
  def apply(stats: Var[SystemStats]) = new SystemStatsView(stats)
}

class SystemStatsView(val stats: Var[List[Var[SystemStats]]]) {
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

  val systemStatsSection =
    div(cls:=BootstrapCSS.panel_panel_default)(
      div(cls:=BootstrapCSS.panel_heading)(
        b("Top system stats"),
        " 13:30:41:50",
        table(cls:=BootstrapCSS.bstable)(
          tr(
            th("PID"), th("COMMAND"), th("%CPU"), th("TIME"), th("MEM")
          ),
          tr(
            td(4148), td("Google Chrome"), td(0.0), td("00:01.82"), td("47M")
          ),
          tr(
            td(4148), td("Google Chrome"), td(0.0), td("00:01.82"), td("47M")
          )
        )
      )
    )

  def render = systemStatsSection.render
}