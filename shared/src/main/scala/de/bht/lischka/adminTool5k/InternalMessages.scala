package de.bht.lischka.adminTool5k

import akka.actor.ActorRef
import de.bht.lischka.adminTool5k.ModelX.{User, WSMessage}

/**
  * Internal Messages are really to be treated as internal.
  * Before sending over Socket, the content of the case
  * class (a WSMessage) has to be unwrapped.
  *
  * good: case SendMessage(m: WSMssage) => proxy ! m
  * bad: case bad: SendMessage => proxy ! bad
  *
  */
object InternalMessages {
  case class SendMessage(msg: WSMessage)

  case class PickledMessageForSending(msg: String)

  case class UnpickledMessageFromNetwork(msg: WSMessage)

  case class RegisterListener(actor: ActorRef)

  case object RequestReplay
}
