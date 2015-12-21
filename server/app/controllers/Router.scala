package controllers

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.ModelX.{ExecuteCommand, WSMessage}

object Router {
  def props(out: ActorRef) = Props(new Router())
}

class Router extends Actor {
  override def receive: Actor.Receive = {
    case wsMessage: WSMessage =>
      wsMessage match {
        case x: ExecuteCommand => println(s"Not implemented: Client, Router receive wsmessage ${x}")
        case _ => println(s"Triggered default case in Router")
      }
  }
}