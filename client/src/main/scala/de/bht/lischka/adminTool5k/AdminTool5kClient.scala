package de.bht.lischka.adminTool5k

import java.util.Date
import akka.actor.Actor.Receive
import akka.actor._
import akka.event.Logging
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.ui.screens.{MainScreen, LoginScreen}
import de.bht.lischka.adminTool5k.ws.WebsocketProxyClient
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}
import scala.scalajs.js
import prickle._
import scala.util.{Failure, Success, Try}
import org.scalajs.jquery.{jQuery => jQ, _}

object Application {
  def props() = Props(new Application())
}

class Application extends Actor {
  val invalidMessageChannel = context.actorOf(InvalidMessageChannel.props)
  val router = context.actorOf(Router.props)
  val websocketProxy = context.actorOf(WebsocketProxyClient.props, "wsproxy")
  val session = context.actorOf(Session.props(websocketProxy, router), "session")
  val loginScreen = context.actorOf(LoginScreen.props(session), "loginScreen")
  val mainScreen = context.actorOf(MainScreen.props(router, session), "mainScreen")

  override def receive: Receive = {
    case anyMessage => session ! anyMessage
  }
}

object AdminTool5kClient extends js.JSApp {
  import ModelX._

  def main(): Unit = {
    val system = ActorSystem("adminTool5k-client")
    system.actorOf(Application.props)
  }
}
