package controllers.pickling

import akka.actor.{Props, Actor}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.WSMessage

trait PickleSupport {
  actor: Actor =>
  val pickling = context.actorOf(Pickling.props)
  val unpickling = context.actorOf(Unpickling.props())

  def handlePickling: Receive = {
    case serializedString: String => unpickling ! serializedString
    case SendMessage(msg: WSMessage) => pickling ! msg
  }
}
