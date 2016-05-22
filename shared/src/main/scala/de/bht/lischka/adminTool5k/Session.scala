package de.bht.lischka.adminTool5k

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages._
import de.bht.lischka.adminTool5k.Session.{Replay, GetUser}
import de.bht.lischka.adminTool5k.pickling.PickleSupport

object Session {
  def props(websocketOut: ActorRef, router: ActorRef) = Props(new Session(websocketOut, router))
  case object GetUser
  case class Replay(replay: List[SendMessage])
}

class Session(websocketProxy: ActorRef, router: ActorRef) extends Actor with PickleSupport {
  import ModelX._


  override def preStart = {
    router ! RegisterListener(self)
  }

  override def receive: Receive = loggedOut

  def loggedOut: Receive = handlePickling orElse {
    /**
     *  TODO: This just lets everything through, but was meant to block
      *  messages when logged out.
     */
    case LoginUser(user) =>
      context become loggedIn(user)
      router ! LoginUser(user)

    //Acts as filter
    case PickledMessageForSending(msg: String) => //websocketProxy ! msg

    //Acts as filter
    case u: SystemStatsUpdateRaw => //Do not log

    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) => self ! wsMessage

    case anyMsg => println(s"Got ${anyMsg} in logged in state")
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) => router ! wsMessage

    case PickledMessageForSending(msg: String) => websocketProxy ! msg

    case Replay(replay) => replay.reverse.foreach((msg: SendMessage) => self ! msg)

    case GetUser => sender ! user

    case anyMsg => println(s"Got ${anyMsg} in logged in state")
  }
}
