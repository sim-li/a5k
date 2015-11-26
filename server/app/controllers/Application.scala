package controllers

import controllers.Application.WebsocketProxyServer.UnpickleRawMessage
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import play.api.Play.current
import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}
import play.api.Logger
import prickle._

import scala.util.{Failure, Success}

object Application extends Controller {
  import ModelX._
  import ModelX.Picklers._
  val system = ActorSystem("adminTool5k-server")
  val router = system.actorOf(Props[Router], "router")

  object WebsocketProxyServer {
    def props(out : ActorRef) = Props(new WebsocketProxyServer(out))
    // INTELLIJIDEA: In Client MessageEvent, could Wrap that up into Supertype and share C. Class
    case class UnpickleRawMessage(e: String)
  }

  class WebsocketProxyServer(out: ActorRef) extends Actor {
    val UNPICKLE_FAIL = "Got failure unpickling WS Message [Server]"
    def p(x: Any) = print(x)

    override def receive: Receive = {
      case UnpickleRawMessage(str) => Unpickle[WSMessage].fromString(str) match { //Repetive, move to shared section
        case Success(m: WSMessage) =>
          println("(Server) unpickled a raw message and forwarded it")
          self ! m
        case Failure(_) => p(UNPICKLE_FAIL)
      }
      case SendMessage(m: WSMessage) => //Repetive, move to shared section
        out ! Pickle.intoString(m)
      case unpickledMessage: WSMessage =>
        p(s"(Server) Got unpickled WS Message ${unpickledMessage}")
        router ! unpickledMessage

      case rawMessage: String => self ! UnpickleRawMessage(rawMessage)

      case _ =>
        println("(Server) Default case")
    }
  }

  object Router {
    def props(out: ActorRef) = Props(new Router())
  }

  class Router extends Actor {
    override def receive: Actor.Receive = {
      case wsMessage: WSMessage =>
        wsMessage match {
          case Hello(m) =>
            println("Got client Hello on Server --> Content" + m)
            sender ! SendMessage(Hello("server"))
        }

    }
  }

  def socket = WebSocket.acceptWithActor[String, String] {
      request: RequestHeader =>
        out: ActorRef =>
           WebsocketProxyServer.props(out)
    }
}
