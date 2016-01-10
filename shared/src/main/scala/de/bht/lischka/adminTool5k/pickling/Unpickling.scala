package de.bht.lischka.adminTool5k.pickling

import akka.actor.{ActorRef, Props, Actor}
import de.bht.lischka.adminTool5k.pickling
import de.bht.lischka.adminTool5k.InternalMessages.{UnpickledMessageFromNetwork, SendMessage}
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
          sender ! UnpickledMessageFromNetwork(unpickledMessage)
        case Failure(ex) =>
          ex.printStackTrace()
      }
  }
}
