package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{LoggedIn, LoggedOut, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{User, ShellCommand, ExecuteCommand, WSMessage}
import de.bht.lischka.adminTool5k.Router.RegisterUiComponent

object Router {
  def props = Props(new Router())

  case class RegisterUiComponent(actor: ActorRef)
}

class Router() extends Actor {
  val proxy = context.actorOf(Props(classOf[WebsocketProxyClient]), "websocketProxyClient")

  var uiComponents = List[ActorRef]()

  def forwardToAll(message: Any) = {
    uiComponents.filter(recipient => recipient != sender).foreach(component => component ! message)
  }

  override def receive: Actor.Receive = {
    case RegisterUiComponent(actor: ActorRef) =>
      uiComponents = actor :: uiComponents

    case sendMsg: SendMessage => proxy ! sendMsg

    case LoggedIn(user: User) => forwardToAll(LoggedIn(user))

    case LoggedOut(user: User) => forwardToAll(LoggedOut(user))
  }
}
