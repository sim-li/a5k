package de.bht.lischka.adminTool5k

import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, LoggedIn}
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, User}
import org.scalajs.jquery.{jQuery => jQ, _}

object LoginScreen {
  def props(router: ActorRef) = Props(new LoginScreen(router))
}

class LoginScreen(router: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
    router ! RegisterListener(self)
  }

  def registerCallback() = {
    jQ("#login_button") click {
      (event: JQueryEventObject) =>
        val loginTextfield = jQ("#login_textfield")
        def userName = loginTextfield.value.toString()
        def validUsername = userName.length() > 0
        if (validUsername) {
          self ! LoginUser(User(userName))
        }
    }
  }

  override def receive: Receive = {
    case LoginUser(user: User) =>
      jQ("#login_container").hide()
      router ! LoginUser(user)
  }
}
