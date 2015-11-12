package controllers
import play.api.Play.current
import akka.actor.{Actor, Props, ActorRef}
import models.WSCommunicationCmds
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}

object Application extends Controller {

  object UserActor {
    def props(out : ActorRef) = Props(new UserActor(out))
  }

  class UserActor(out: ActorRef) extends Actor {
    override def receive: Receive = {
      case json: JsValue =>
        println(s"Hey, I got a really pretty pretty JSON! Oh wuhu! ${json}")
      case _ =>
        println("Got something, who knows what.")
    }
  }

  def socket = WebSocket.acceptWithActor[JsValue, JsValue] {
    request: RequestHeader =>
      out: ActorRef =>
        UserActor.props(out)
  }
}
