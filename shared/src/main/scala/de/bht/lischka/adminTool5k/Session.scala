package de.bht.lischka.adminTool5k

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.{SendMessage, UnpickledMessageFromNetwork, RegisterListener, PickledMessageForSending}
import de.bht.lischka.adminTool5k.pickling.PickleSupport

object Session {
  def props(websocketOut: ActorRef, router: ActorRef) = Props(new Session(websocketOut, router))
  case class ReceiveMessage(e: String)
}

class Session(websocketOut: ActorRef, router: ActorRef) extends Actor with PickleSupport {
  import ModelX._

  override def preStart = {
    router ! RegisterListener(self)
  }

  override def receive: Receive = loggedOut

  def loggedOut: Receive = handlePickling orElse {
    case LoginUser(user) =>
      context become loggedIn(user)
      router ! LoginUser(user)


    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) =>
      wsMessage match {
        case LoginUser(user: User) => self ! LoginUser(user)

        case anyMsg => println(s"Got ${anyMsg} in logged out state")
      }
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) => router ! wsMessage

    case PickledMessageForSending(msg: String) =>
      websocketOut ! PickledMessageForSending(msg)

    case anyMsg => println(s"Got ${anyMsg} in logged out state")
  }
}
