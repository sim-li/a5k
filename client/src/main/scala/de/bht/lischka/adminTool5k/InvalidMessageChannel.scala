package de.bht.lischka.adminTool5k
import akka.actor.{Props, Actor}
import de.bht.lischka.adminTool5k.InternalMessages.InvalidMessage

object InvalidMessageChannel {
  def props = Props(new InvalidMessageChannel())
}

class InvalidMessageChannel extends Actor {
  override def receive: Receive = {
    case invalidMessage: InvalidMessage =>
      println(s"Got invalid message: [s ${invalidMessage.sender} --> ${invalidMessage.receiver}")
      println(s"Message content was ${invalidMessage.message}")
  }
}
