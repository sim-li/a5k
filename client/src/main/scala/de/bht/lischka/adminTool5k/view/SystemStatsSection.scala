package de.bht.lischka.adminTool5k.view

import akka.actor.{Actor, Props}
import de.bht.lischka.adminTool5k.ModelX.{SystemStats, ShellCommand}
import org.scalajs.jquery._
import rx._
import org.scalajs.jquery.{jQuery => jQ, _}

object SystemStatsSection {
  def props(systemStats: SystemStats) = Props(new SystemStatsSection(systemStats))
  case class UpdateEntry(systemStats: SystemStats)
}

class SystemStatsSection(systemStats: SystemStats) extends Actor {
  import SystemStatsSection._

  val view  = SystemStatsView(Var(systemStats))

  override def preStart = {
    jQ("#stats_section").append(view.render)
  }

  def receive: Receive = {
    case UpdateEntry(systemStats: SystemStats) =>
//      if(shellCommand.issueInfo.id  == shellCommandUpdate.issueInfo.id) {
//        view.shellCommand() = shellCommandUpdate
//      }

    case _ => println("Hit default case in view")
  }
}