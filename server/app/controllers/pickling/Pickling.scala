package controllers.pickling

import akka.actor.{Props, Actor}
import controllers.pickling
import de.bht.lischka.adminTool5k.InternalMessages.PickledMessageForSending
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import prickle.Pickle

object Pickling {
  def props = Props(new Pickling())
}

class Pickling extends Actor {
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  override def receive: Receive = {
    case wsMessage: WSMessage => sender ! PickledMessageForSending(Pickle.intoString(wsMessage))
  }
}
