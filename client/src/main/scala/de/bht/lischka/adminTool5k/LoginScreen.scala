package de.bht.lischka.adminTool5k

import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.LoggedIn
import de.bht.lischka.adminTool5k.ModelX.User
import de.bht.lischka.adminTool5k.Router.RegisterUiComponent
import org.scalajs.jquery.{jQuery => jQ, _}

object LoginScreen {
  def props(router: ActorRef) = Props(new LoginScreen(router))
}

class LoginScreen(router: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
    router ! RegisterUiComponent(self)
  }

  def registerCallback() = {
    jQ("#login_button") click {
      (event: JQueryEventObject) =>
        val loginTextfield = jQ("#login_textfield")
        def userName = loginTextfield.value.toString()
        def validUsername = userName.length() > 0
        if (validUsername) {
          self ! LoggedIn(User(userName))
        }
    }
  }

  override def receive: Receive = {
    case LoggedIn(user: User) =>
      jQ("#login_container").hide()
      router ! LoggedIn(user)
  }
}