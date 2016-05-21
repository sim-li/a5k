package de.bht.lischka.adminTool5k.ui.screens

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.ui.widgets.commandlist.ShellCommandEntrySection.SetUser
import de.bht.lischka.adminTool5k.ui.widgets.commandlist.{ShellCommandEntrySection, ShellCommandEntry}
import de.bht.lischka.adminTool5k.ui.widgets.stats.SystemStatsSection.SystemStatsUpdate
import de.bht.lischka.adminTool5k.ui.widgets.stats.{SystemStatsSection}
import org.scalajs.jquery.{jQuery => jQ, _}

object MainScreen {
  def props(router: ActorRef, session: ActorRef) = Props(new MainScreen(router, session))

}

class MainScreen(router: ActorRef, session: ActorRef) extends Actor {
  val systemStatsSection: ActorRef = context.actorOf(SystemStatsSection.props, "system-stats-section")
  val shellCommandEntrySection: ActorRef = context.actorOf(ShellCommandEntrySection.props, "shell-command-entry-section")

  override def preStart: Unit = {
    router ! RegisterListener(self)
  }

  override def receive: Receive = loggedOut

  // View must handle incomming msgs when logged out for command replay
  def commonOps: Receive = {
    case executeCommand: ExecuteCommand => shellCommandEntrySection ! executeCommand

    case commandResult: CommandResult => shellCommandEntrySection ! commandResult
  }

  def loggedOut: Receive = commonOps orElse {
    case LoginUser(user: User) =>
      context.become(loggedIn(user))
      shellCommandEntrySection ! SetUser(user)
      jQ("#main_container").show()
  }

  def loggedIn(user: User): Receive = commonOps orElse {
    case update: SystemStatsUpdate => systemStatsSection ! update

    case messageOut: SendMessage => router ! messageOut

    case x => println(s"Mainscreen hit default case ${x}")
  }
}
