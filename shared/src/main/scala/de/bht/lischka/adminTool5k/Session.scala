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

class Session(websocketOut: ActorRef, router: ActorRef) extends Actor with PickleSupport {
  import ModelX._


  override def preStart = {
    router ! RegisterListener(self)
  }

  override def receive: Receive = loggedOut

  /**
    * Pickling reacts to SendMessage
    */
  def loggedOut: Receive = handlePickling orElse {
    case LoginUser(user) =>
      println("Got login user at session")
      context become loggedIn(user)
      router ! LoginUser(user)
      // TODO: Move this to Router or something more specific, triggers defualt case on client
      router ! RequestReplay

    /**
     *  TODO: This just lets everything through, but was meant to block
      *  messages when logged out.
     */
    case PickledMessageForSending(msg: String) =>
      websocketOut ! msg

    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) =>
      wsMessage match {
        case LoginUser(user: User) =>
          self ! LoginUser(user)

        case u: SystemStatsUpdateRaw => println(s"Got sysStatsUpdate, ignoring it: $u")//Do not log, blocks browser.

        case anyMsg => println(s"Got ${anyMsg} in logged out state")
      }
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case UnpickledMessageFromNetwork(wsMessage: WSMessage ) => router ! wsMessage

    case PickledMessageForSending(msg: String) => websocketOut ! msg

    case Replay(replay) => replay.reverse.foreach((msg: SendMessage) => self ! msg)

    case GetUser => sender ! user

    case anyMsg => println(s"Got ${anyMsg} in logged in state")
  }
}
