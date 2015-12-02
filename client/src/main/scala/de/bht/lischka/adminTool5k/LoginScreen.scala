package de.bht.lischka.adminTool5k

import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.LoginScreen.{HideScreen, UserLoggedIn}
import de.bht.lischka.adminTool5k.ModelX.User
import org.scalajs.jquery.{jQuery => jQ, _}

object LoginScreen {
  def props(userState: ActorRef, mainScreen: ActorRef) = Props(new LoginScreen(userState, mainScreen))
  case object UserLoggedIn
  case object HideScreen
}

class LoginScreen(userState: ActorRef, mainScreen: ActorRef) extends Actor {
  override def preStart: Unit = {
    registerCallback()
  }

  def registerCallback() = {
    jQ("#login_button") click {
      (event: JQueryEventObject) =>
        val loginTextfield = jQ("#login_textfield")
        def userName = loginTextfield.value.toString()
        def validUsername = userName.length() > 0
        if (validUsername) {
          userState ! User(userName)
        }
    }
  }

  override def receive: Receive = {
    case UserLoggedIn =>
      self ! HideScreen
      mainScreen ! MainScreen.ShowScreen
    case HideScreen =>
      println("Login screen got hide message")
      jQ("#login_container").hide()
  }
}