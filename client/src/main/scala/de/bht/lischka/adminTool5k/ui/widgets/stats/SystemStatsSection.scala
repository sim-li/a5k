package de.bht.lischka.adminTool5k.ui.widgets.stats

import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{Pid, ShellCommand, SystemStatsUpdate, SystemStatsEntry}
import org.scalajs.jquery.{jQuery => jQ}

object SystemStatsSection {
  def props = Props(new SystemStatsSection())
}

class SystemStatsSection() extends Actor {
  var statsEntries: Map[Pid, ActorRef] = Map()

  def receive: Receive = {
    case SystemStatsUpdate(allEntries: List[SystemStatsEntry]) =>
      allEntries.foreach((entry: SystemStatsEntry) => entry.pid.foreach(pid => statsEntries.get(pid) match {
          case Some(existingEntry: ActorRef) => existingEntry ! entry
          case None => statsEntries += pid -> context.actorOf(SystemStatsEntryActor.props(entry))
        }
      ))
    case _ => println("Unhandled default case")
  }
}
