package controllers.pickling

import akka.actor.{Props, Actor}
import controllers.pickling
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import prickle.{Pickle, Unpickle}

import scala.util.{Failure, Success}

object Unpickling {
  def props = Props(new Unpickling())
}

class Unpickling extends Actor {
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  override def receive: Receive = {
    case rawMessage: String =>
      println("Got a raw message")
      Unpickle[WSMessage].fromString(rawMessage) match {
        case Success(unpickledMessage: WSMessage) =>
          println(s"Got unpickled message named ${unpickledMessage}")
          self ! unpickledMessage
        case Failure(_) =>
        // @TODO: Handle unpickling failure here!
      }
    case SendMessage(wsMessage: WSMessage) =>
      val pickleResult = Pickle.intoString(wsMessage)
      sender ! PickleSupport.PickleResult(pickleResult)
  }
}
