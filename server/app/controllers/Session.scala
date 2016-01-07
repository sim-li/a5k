package controllers

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.pickling.PickleSupport

object Session {
  def props(websocketOut: ActorRef, router: ActorRef) = Props(new Session(websocketOut, router))
  case class ReceiveMessage(e: String)
}

/**
  * Session represents a Websocket Connection opened by a client
  * and changes it's state if a LoginUser message is received.
  *
  * We will only forward messages with Session (and allow interaction with the server),
  * if the user is logged in.
  *
  * WebsocketProxyServer is started with Akka's Default Actor System,
  * it follows the application's life-cycle
  * https://www.playframework.com/documentation/2.0/ScalaAkka
  *
  * http://stackoverflow.com/questions/17383827/how-do-i-best-share-behavior-among-akka-actors
  * https://twitter.github.io/scala_school/pattern-matching-and-functional-composition.html
  * http://www.scala-lang.org/api/2.10.3/index.html#scala.PartialFunction
  *
  * Receive is a partial function (pattern matching).
  *
  * @param websocketOut
  * @param router
  */
class Session(websocketOut: ActorRef, router: ActorRef) extends Actor with PickleSupport {
  import ModelX._

  override def receive: Receive = loggedOut

  def loggedOut: Receive = handlePickling orElse {
    case LoginUser(user: User) =>
      context become loggedIn(user)
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case wsMessage: WSMessage => router ! wsMessage
    case PickledMessageForSending(msg: String) => websocketOut ! msg
    case _ => println("Got default case")
  }
}





