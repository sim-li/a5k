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
    case LoginUser(user: User) =>
      println("Got Loginuser message in Session")
      context become loggedIn(user)
      self ! LoginUser(user)

    case _ =>
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) => router ! wsMessage

    case PickledMessageForSending(msg: String) =>
      println(s"Session sending out $msg to websocket")
      websocketOut ! PickledMessageForSending(msg)

    case LoginUser(user) =>
      println("Forwarded login user message from Session to router + overWebScoket")
      router ! LoginUser(user)
      self ! SendMessage(LoginUser(user))
  }
}
