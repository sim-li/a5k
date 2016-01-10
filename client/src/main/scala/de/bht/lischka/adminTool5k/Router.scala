package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, LoggedIn, LoggedOut, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._

object Router {
  def props = Props(new Router())
}

class Router extends Actor {
  var uiComponents = List[ActorRef]()

  def forwardToAll(message: Any) = {
    uiComponents.filter(recipient => recipient != sender).foreach(component => component ! message)
  }

  override def receive: Actor.Receive = {
    case RegisterListener(actor: ActorRef) =>
      uiComponents = actor :: uiComponents

    /**
      * The Session will send out every message wrapped in SendMessage.
      *
      */
    case LoginUser(user: User) => forwardToAll(LoginUser(user))

    case wsMessage: WSMessage => forwardToAll(SendMessage(wsMessage))
  }
}
