package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.WebsocketProxyClient.{UnpickleFromMessageEvent, ConnectionClosed, Error}
import org.scalajs.dom
import org.scalajs.dom._
import prickle.{Pickle, Unpickle}
import scala.util.{Failure, Success}

object WebsocketProxyClient {
  def props: Props = Props(new WebsocketProxyClient())

  case class ConnectionEstablished(socket: dom.WebSocket)

  case object ConnectionClosed

  case class Error(e: Event)

  case class UnpickleFromMessageEvent(e: MessageEvent)
}

class WebsocketProxyClient() extends Actor {
  import context._
  import de.bht.lischka.adminTool5k.ModelX._
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  override def preStart = {
    registerCallbacks()
  }

  def registerCallbacks() = {
    val url = "ws://localhost:9000/ws-entry"
    val socket = new dom.WebSocket(url)
    socket.onopen = { (e: Event) => self ! socket } // Wrap Socket?
    socket.onmessage = { (e: MessageEvent) => self ! WebsocketProxyClient.UnpickleFromMessageEvent(e) }
    socket.onclose = { (e: Event) => self ! WebsocketProxyClient.ConnectionClosed }
    socket.onerror = { (e: Event) => WebsocketProxyClient.Error(e) }
  }

  override def receive: Receive = disconnected

  def disconnected: Receive = {
    case socket: dom.WebSocket =>
      context.become(connected(socket))
  }

  def closed: Receive = {
    case _ => println("Unimplemented WS close [Client]")
  }

  def reconnecting: Receive = {
    case _ => println("Unimplemented WS reconnecting [Client]")
  }

  def connected(socket: dom.WebSocket): Receive = {
    case UnpickleFromMessageEvent(messageEvent: MessageEvent) => unpickleStrAndForward(messageEvent.data.toString(), context.parent)

    case SendMessage(wsMessage: WSMessage) =>
      println("Got a send message command in proxy")
      socket.send(Pickle.intoString(wsMessage))

    case ConnectionClosed => become(closed)

    case Error(e: Event) => println("Got an error, babe [Client]")

    case _ => println("Default case [Client]")
  }

  def unpickleStrAndForward(str: String, forwardingRef: ActorRef) = {
    Unpickle[WSMessage].fromString(str) match {
      case Success(m: WSMessage) =>
        forwardingRef ! m
      case Failure(_) =>
    }
  }
}