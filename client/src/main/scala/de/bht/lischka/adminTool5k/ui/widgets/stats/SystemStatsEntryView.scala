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

  //@TODO: Replace this repetitive pattern, switch to actors/jquery
  val processName: Rx[String] = Rx {
    stats().processName match {
      case Some(n) => n.toString
      case _ => ""
    }
  }

  val pid: Rx[String] = Rx {
    stats().pid match {
      case Some(p) => p.toString
      case _ => ""
    }
  }

  val cpu: Rx[String] = Rx {
    stats().cpu match {
      case Some(c) => c.toString
      case _ => ""
    }
  }

  val time: Rx[String] = Rx {
    stats().time match {
      case Some(t) => t.toString
      case _ => ""
    }
  }

  val  memoryUsage: Rx[String] = Rx {
    stats().memoryUsage match {
      case Some(m) => m.toString
      case _ => ""
    }
  }

  val systemStatsSection = tr(
    td(pid),
    td(processName),
    td(cpu),
    td(time),
    td(memoryUsage)
  )

  def render = systemStatsSection.render
}
