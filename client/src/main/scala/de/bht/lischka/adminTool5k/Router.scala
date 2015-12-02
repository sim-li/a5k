package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.{ShellCommand, ExecuteCommand, WSMessage}

object Router {
  def props = Props(new Router())
}

class Router() extends Actor {
  val proxy = context.actorOf(Props(classOf[WebsocketProxyClient]), "websocketProxyClient")

  override def receive: Actor.Receive = {
    case wsMessage: WSMessage =>
      wsMessage match {
        case ExecuteCommand(_) =>
          println("Forwarding executeCommand to proxy")
          proxy ! ExecuteCommand
      }
  }
}
