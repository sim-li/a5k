package controllers.pickling

import akka.actor.{ActorRef, Props, Actor}
import controllers.pickling
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import prickle.{Pickle, Unpickle}

import scala.util.{Failure, Success}

object Unpickling {
  def props() = Props(new Unpickling())
}

class Unpickling() extends Actor {
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  override def receive: Receive = {
    case rawMessage: String =>
      Unpickle[WSMessage].fromString(rawMessage) match {
        case Success(unpickledMessage: WSMessage) =>
          sender ! unpickledMessage

        case Failure(ex) =>
          ex.printStackTrace()
      }
  }
}