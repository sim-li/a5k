package controllers

import akka.actor.{Actor, Props, ActorRef}
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers, RegisterReceiver}
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, WSMessage}

object Router {
  def props = Props(new Router())
  case class RegisterReceiver(session: ActorRef)
  case object TestListReceivers
  case class TestSendNewsToAllReceivers(news: Any)
}

class Router extends Actor {
  var receivers = List[ActorRef]()

  override def receive: Actor.Receive = {
    case RegisterReceiver(session: ActorRef) =>
      receivers = session :: receivers

    case TestListReceivers =>
      sender ! receivers

    case TestSendNewsToAllReceivers(news: Any) =>
      receivers.map(receiver => receiver ! news)



    case wsMessage: WSMessage =>
      wsMessage match {
        case x: ExecuteCommand => println(s"Not implemented: Client, Router receive wsmessage ${x}")
        case _ => println(s"Triggered default case in Router")
      }
  }
}