package de.bht.lischka.adminTool5k.ui.screens

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.RegisterListener
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, User}
import de.bht.lischka.adminTool5k.ui.screens.LoginScreen.LoginButtonClick
import de.bht.lischka.adminTool5k.ws.WebsocketProxyClient
import de.bht.lischka.adminTool5k.ws.WebsocketProxyClient.SocketOpen
import org.scalajs.jquery.{jQuery => jQ, _}

object LoginScreen {
  def props(session: ActorRef, websocketProxy: ActorRef) = Props(new LoginScreen(session, websocketProxy))
  case object LoginButtonClick
}

class LoginScreen(session: ActorRef, websocketProxy: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
    session ! RegisterListener(self)
  }

  def registerCallback() = {
    jQ("#login_button") click {
      (event: JQueryEventObject) => self ! LoginButtonClick
    }
  }

  override def receive: Receive = {
    case LoginButtonClick =>
      val ip = jQ("#websocket_textfield").value.toString
      websocketProxy ! WebsocketProxyClient.OpenSocket(ip)

    case SocketOpen =>
      val loginTextfield = jQ("#login_textfield")
      val userName = loginTextfield.value.toString()
      if (userName.length() > 0) {
        jQ("#login_container").hide()
        session ! LoginUser(User(userName))
     }
  }
}
