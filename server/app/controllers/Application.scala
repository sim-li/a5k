package controllers

import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import play.api.Play.current
import akka.actor.{Actor, Props, ActorRef}
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}
import play.api.Logger
import prickle._

object Application extends Controller {
  import ModelX._
  import ModelX.Picklers._
  object UserActor {
    def props(out : ActorRef) = Props(new UserActor(out))
  }

  class UserActor(out: ActorRef) extends Actor {
    def processStrFromClient(str: String) = {
      def msg = Unpickle[WSMessage].fromString(str)
      println(s"Depickle is ${msg} at ${new java.util.Date()}")
    }

    override def receive: Receive = {
      case str: String =>
       processStrFromClient(str)
      case _ =>
    }
    override def postStop() = {
    }
  }

  def socket = WebSocket.acceptWithActor[String, String] {
      request: RequestHeader =>
        out: ActorRef =>
           UserActor.props(out)
    }
}
