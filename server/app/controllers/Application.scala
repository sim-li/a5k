package controllers
import play.api.Play.current
import akka.actor.{Actor, Props, ActorRef}
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}
import play.api.Logger

object Application extends Controller {

  object UserActor {
    def props(out : ActorRef) = Props(new UserActor(out))
  }

  class UserActor(out: ActorRef) extends Actor {
    override def receive: Receive = {
      case json: JsValue =>
        println(s"Hey, I got a really pretty pretty JSON! Oh wuhu! ${json}")
        out ! "Fuck you"
      case _ =>
    }
    override def postStop() = {
    }
  }

  def socket = WebSocket.acceptWithActor[JsValue, JsValue] {
      request: RequestHeader =>
        out: ActorRef =>
           UserActor.props(out)
    }
}
