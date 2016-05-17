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
    case SystemStatsUpdate(systemStatsLine: List[SystemStatsLine]) =>
//      systemStatsLine.pid.foreach(pid => statsEntries.get(pid) match {
//
//
//
//          case Some(l: ActorRef) => l ! SystemStatsUpdate(systemStatsLine)
//          case None => statsEntries += pid -> context.actorOf(SystemStatsEntry.props(systemStatsLine))
//        }
//      )
    case _ => println("Unhandled defualt case")
  }

}
