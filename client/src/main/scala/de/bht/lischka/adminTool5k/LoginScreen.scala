package de.bht.lischka.adminTool5k

import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener}
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, User}
import org.scalajs.jquery.{jQuery => jQ, _}

object LoginScreen {
  def props(session: ActorRef) = Props(new LoginScreen(session))
}

class LoginScreen(session: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
    session ! RegisterListener(self)
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
      session ! LoginUser(user)
  }
}
