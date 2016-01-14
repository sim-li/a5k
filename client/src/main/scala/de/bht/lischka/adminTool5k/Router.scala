package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._

object Router {
  def props = Props(new Router())
}

class Router extends Actor {
  var registeredReceivers = List[ActorRef]()

  def forwardMsgToAll(message: Any) = {
    forwardMsgToAllBut(message, sender)
  }

  def forwardMsgToAllBut(message: Any, ignoredRecipient: ActorRef) = {
    registeredReceivers.filter(p => p != ignoredRecipient).foreach(component => component ! message)
  }

  override def receive: Actor.Receive = {
    case RegisterListener(actor: ActorRef) =>
      registeredReceivers = actor :: registeredReceivers

    case LoginUser(user: User) =>
      val session = sender()
      session ! SendMessage(LoginUser(user))
      forwardMsgToAll(LoginUser(user))

    case sendMessage: SendMessage => forwardMsgToAll(sendMessage)
    case wsMessage: WSMessage => forwardMsgToAll(SendMessage(wsMessage))

  }
}
