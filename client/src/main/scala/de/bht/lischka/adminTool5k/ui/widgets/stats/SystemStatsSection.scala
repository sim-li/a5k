package de.bht.lischka.adminTool5k.ui.widgets.stats

import java.util.UUID
import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Pid, ShellCommand, SystemStatsUpdate, SystemStatsLine}
import org.scalajs.jquery.{jQuery => jQ}
import rx._
import scala.None

object SystemStatsSection {
  def props = Props(new SystemStatsSection())
}

class SystemStatsSection() extends Actor {
  import SystemStatsEntry._

  var statsEntries: Map[Pid, ActorRef] = Map()

  def receive: Receive = {
    case SystemStatsUpdate(systemStatsLine: SystemStatsLine) =>
      systemStatsLine.pid match {
        case Some(pid) => statsEntries.get (pid) match {
            case Some(entry: ActorRef) => updateEntry(entry, systemStatsLine)

            case None => addSystemStatsEntry(pid, systemStatsLine)
        }
        case None => throw new RuntimeException(s"Got update entry command without PID")
      }
  }

  def updateEntry(entry: ActorRef, systemStatsLine: SystemStatsLine): Unit = {
    entry ! SystemStatsUpdate(systemStatsLine)
  }

  def addSystemStatsEntry(pid: Pid, systemStatsLine: SystemStatsLine): Unit = {
    systemStatsLine.pid match {
      case Some(pid) =>
        statsEntries += pid -> context.actorOf(SystemStatsEntry.props(systemStatsLine))
      case None  =>
        throw new RuntimeException
    }
  }
}
