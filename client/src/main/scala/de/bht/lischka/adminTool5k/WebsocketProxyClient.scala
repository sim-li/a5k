package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.WebsocketProxyClient._
import org.scalajs.dom
import org.scalajs.dom._
import prickle.{Pickle, Unpickle}
import scala.util.{Failure, Success}

object WebsocketProxyClient {
  def props: Props = Props(new WebsocketProxyClient())

  case class ConnectionEstablished(socket: dom.WebSocket)

  case object ConnectionClosed

  case class Error(e: Event)

  case class ReceiveMessage(e: MessageEvent)
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
    socket.onopen = { (e: Event) => self ! ConnectionEstablished(socket) }
    socket.onmessage = { (e: MessageEvent) => self ! ReceiveMessage(e) }
    socket.onclose = { (e: Event) => self ! ConnectionClosed }
    socket.onerror = { (e: Event) => self ! Error(e) }
  }

  override def receive: Receive = disconnected

  def disconnected: Receive = {
    case ConnectionEstablished(socket: dom.WebSocket) => context.become(connected(socket))
  }

  def closed: Receive = {
    case _ => println("Unimplemented WS close [Client]")
  }

  def reconnecting: Receive = {
    case _ => println("Unimplemented WS reconnecting [Client]")
  }

  def connected(socket: dom.WebSocket): Receive = {
    case ReceiveMessage(messageEvent: MessageEvent) =>
      unpickleStrAndForward(messageEvent.data.toString(), context.parent)

    case SendMessage(x) =>
      socket.send(Pickle.intoString(x))

    case ConnectionClosed => become(closed)

    case Error(e: Event) => println("Error [Client]")

    case d => println(s"Default case [Client] with ${d}")
  }

  def unpickleStrAndForward(str: String, forwardingRef: ActorRef) = {
    Unpickle[WSMessage].fromString(str) match {
      case Success(m: WSMessage) =>
        forwardingRef ! m
      case Failure(_) =>
    }
  }
}
