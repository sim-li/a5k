package controllers

import akka.actor.{Actor, Props, ActorRef}
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers}
import de.bht.lischka.adminTool5k.InternalMessages.RegisterListener
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, ExecuteCommand, WSMessage}
import sys.process._

object Router {
  def props = Props(new Router())
  case object TestListReceivers
  case class TestSendNewsToAllReceivers(news: Any)
}

class Router extends Actor {
  var receivers = List[ActorRef]()

  override def receive: Actor.Receive = {
    case RegisterListener(session: ActorRef) =>
      receivers = session :: receivers

    case TestListReceivers =>
      sender ! receivers

    case TestSendNewsToAllReceivers(news: Any) =>
      receivers.map(receiver => receiver ! news)

    case wsMessage: WSMessage =>
      wsMessage match {
        case ExecuteCommand(shellCommand) =>
          val cmd: String = shellCommand.command
          val commandResult = cmd.!!
          println(s"Command result is ${commandResult}")

        case anything => println(s"Triggered default case in Router, got ${anything}")
      }
  }
}