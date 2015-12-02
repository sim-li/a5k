package de.bht.lischka.adminTool5k

import akka.actor.{Props, Actor, ActorRef}
import de.bht.lischka.adminTool5k.ModelX.User

object UserState {
  def props(router: ActorRef) = Props(new UserState(router))
}

class UserState(router: ActorRef) extends Actor with akka.actor.ActorLogging {
  var user = User.none

  override def receive: Receive = {
    case user: User =>
      this.user = user
      println(s"Logged in user ${user}")
      sender ! LoginScreen.UserLoggedIn
  }
}
