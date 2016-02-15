package de.bht.lischka.adminTool5k.ui.widgets.stats

import java.util.UUID

import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Pid, ShellCommand, SystemStatsUpdate, SystemStatsLine}
import de.bht.lischka.adminTool5k.ui.widgets.commandlist.ShellCommandEntry
import org.scalajs.jquery.{jQuery => jQ}
import rx._

import scala.None

object SystemStatsSection {
  def props = Props(new SystemStatsSection())
}

class SystemStatsSection() extends Actor {
  import SystemStatsEntry._

  var statsEntries: Map[Pid, ActorRef] = Map()

  val view  = SystemStatsEntryView(Var(systemStats))

  override def preStart = {
    jQ("#stats_entry").append(view.render)
  }

  def receive: Receive = {
    case SystemStatsUpdate(stats: SystemStatsLine) =>


    case UpdateEntry(systemStatsUpdate: SystemStatsLine) =>
      systemStatsUpdate.pid match {
        case Some(pid) =>
          if(statsEntries.contains(pid)) {
            statsEntries // UPDATE BLA BLA
          } else {
            addSystemStatsEntry(pid, systemStatsUpdate)
          }
      }

      if(systemStats.pid == systemStatsUpdate.pid) {
        view.stats() = systemStatsUpdate
      }
    case _ => println("Hit default case in view")
  }

  def addSystemStatsEntry(pid: Pid, systemStatsUpdate: SystemStatsLine): Unit = {
    systemStatsUpdate.pid match {
      case Some(pid) =>
        statsEntries += pid -> context.actorOf(SystemStatsEntry.props(systemStatsUpdate))
      case None  =>
        throw new RuntimeException
    }

  }
}