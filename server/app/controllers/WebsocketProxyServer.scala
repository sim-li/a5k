package controllers

import akka.actor.{Actor, ActorRef, Props}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import prickle.{Pickle, Unpickle}

import scala.util.{Failure, Success}

object WebsocketProxyServer {
  def props(websocketOut: ActorRef, router: ActorRef) = Props(new WebsocketProxyServer(websocketOut, router))
  case class ReceiveMessage(e: String)
}

class WebsocketProxyServer(websocketOut: ActorRef, router: ActorRef) extends Actor {
  import ModelX._
  import ModelX.Picklers._

  import WebsocketProxyServer._
  override def receive: Receive = {
    case ReceiveMessage(str) => unpickleStrAndForward(str, self)
    case SendMessage(wsMessage: WSMessage) => pickleToStrAndForward(wsMessage, websocketOut)
    case wsMessage: WSMessage => router ! wsMessage
    case rawMessage: String => self ! ReceiveMessage(rawMessage)
    case defaultCase => logDefaultCaseFor("WebsocketProxyServer", defaultCase)
  }

  def unpickleStrAndForward(str: String, forwardingRef: ActorRef) = {
    Unpickle[WSMessage].fromString(str) match {
      case Success(m: WSMessage) =>
        forwardingRef ! m
      case Failure(_) =>
    }
  }

  def pickleToStrAndForward(wsMessage: WSMessage, forwardingRef: ActorRef) = {
    forwardingRef ! Pickle.intoString(wsMessage)
  }

  def logDefaultCaseFor(identifier: String, msg: Any) = {
    println(s"Hit default case for ${identifier}, with msg ${msg}")
  }
}

