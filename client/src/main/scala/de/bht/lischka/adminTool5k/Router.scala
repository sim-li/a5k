package de.bht.lischka.adminTool5k

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, SendMessage}
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.pidparsing.PidParsingUtils

object Router {
  def props = Props(new Router())
}

class Router extends Actor {
  var registeredReceivers = List[ActorRef]()

    def forwardMsg(message: Any) = {
      val ignoredReceiver = sender()
      registeredReceivers.filter(receiver => receiver != ignoredReceiver).
        foreach((receiver: ActorRef)  => receiver ! message)
    }

  override def receive: Actor.Receive = {
    case RegisterListener(actor: ActorRef) =>
      registeredReceivers = actor :: registeredReceivers

    case LoginUser(user: User) =>
      val session = sender()
      session ! SendMessage(LoginUser(user))
      forwardMsg(LoginUser(user))

    case sendMessage: SendMessage =>
      forwardMsg(sendMessage)

    case systemStatsUpdateRaw: SystemStatsUpdateRaw =>
      val update: List[SystemStatsEntry] = PidParsingUtils(systemStatsUpdateRaw.rawStats).rows
      self ! update

    case wsMessage: WSMessage =>
      forwardMsg(wsMessage)

    case x => println(s"Triggered default case with ${x}")

  }
}
