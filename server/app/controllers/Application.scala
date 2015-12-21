package controllers

import controllers.proxy.WebsocketProxyServer
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.{ShellCommand, ExecuteCommand, WSMessage}
import play.api.Play.current
import akka.actor.{ActorSystem, Actor, Props, ActorRef}
import play.api.libs.json._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}
import play.api.Logger
import scala.util.{Failure, Success}

object Application extends Controller {
  val system = ActorSystem("adminTool5k-server")
  val router = system.actorOf(Props[Router], "router")

  def socket = WebSocket.acceptWithActor[String, String] {
      request: RequestHeader =>
        out: ActorRef =>
           WebsocketProxyServer.props(out, router)
    }
}
