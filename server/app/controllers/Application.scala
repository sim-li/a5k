package controllers

import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.{ShellCommand, ExecuteCommand, WSMessage}
import play.api.Play.current
import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}
import play.api.Logger
import scala.util.{Failure, Success}
import controllers.WebsocketProxyServer

object Application extends Controller {
  val system = ActorSystem("adminTool5k-server")
  val router = system.actorOf(Props[Router], "router")

  object Router {
    def props(out: ActorRef) = Props(new Router())
  }

  class Router extends Actor {
    override def receive: Actor.Receive = {
      case wsMessage: WSMessage =>
        wsMessage match {
          case ExecuteCommand(command: ShellCommand) => println("Not implemented: Client, Router receive wsmessage")
          case x => println(s"Triggered default case with ${x}")
        }
    }
  }

  def socket = WebSocket.acceptWithActor[String, String] {
      request: RequestHeader =>
        out: ActorRef =>
           WebsocketProxyServer.props(out, router)
    }
}
