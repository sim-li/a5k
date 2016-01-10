package controllers

import akka.actor.{Actor, Props, ActorRef}
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers}
import de.bht.lischka.adminTool5k.InternalMessages.RegisterListener
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, WSMessage}

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
        case x: ExecuteCommand => println(s"Not implemented: Server, Router receive wsmessage ${x}")
        case anything => println(s"Triggered default case in Router, got ${anything}")
      }
  }
}