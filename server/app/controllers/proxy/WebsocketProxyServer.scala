package controllers.proxy

import akka.actor.{Actor, ActorRef, Props}
import controllers.pickling
import controllers.pickling.PickleSupport
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import prickle.{Pickle, Unpickle}

import scala.util.{Failure, Success}

object WebsocketProxyServer {
  def props(websocketOut: ActorRef, router: ActorRef) = Props(new WebsocketProxyServer(websocketOut, router))
  case class ReceiveMessage(e: String)
}

/**
  * Websocket Proxy Server represents a user, it gets instantiated for every
  * opened Websocket connection.
  *
  * We will only allow handle requests when a LoginUser method was send.
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
class WebsocketProxyServer(websocketOut: ActorRef, router: ActorRef) extends Actor with PickleSupport {
  import ModelX._

  override def receive: Receive = loggedOut

  def loggedOut: Receive = handlePickling orElse {
    case LoginUser(user: User) =>
      context become loggedIn(user)
  }

  def loggedIn(user: User): Receive = handlePickling orElse {
    case wsMessage: WSMessage => router ! wsMessage
    case _ => println("Got default case")
  }
}





