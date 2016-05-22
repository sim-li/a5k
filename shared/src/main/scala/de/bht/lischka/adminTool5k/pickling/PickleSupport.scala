package de.bht.lischka.adminTool5k.pickling

import akka.actor.Actor
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.WSMessage

/**
  * Naming actor: Actor => with self: Actor overrides sender reference:
  * http://coding-journal.com/why-is-my-akka-actor-getting-deadletters-as-sender/
  */
trait PickleSupport extends Actor {
  val pickling = context.actorOf(Pickling.props)
  val unpickling = context.actorOf(Unpickling.props())

  def handlePickling: Receive = {
    case serializedString: String => unpickling ! serializedString
    case SendMessage(msg: WSMessage) => pickling ! msg
  }
}
