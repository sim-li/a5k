package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef}
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import org.scalajs.dom
import org.scalajs.dom._
import prickle.{Pickle, Unpickle}
import scala.util.{Failure, Success}

object WebsocketProxyClient {
  def props = Props(new WebsocketProxyClient())

  case class ConnectionEstablished(socket: dom.WebSocket)

  case object ConnectionClosed

  case class Error(e: Event)

  case class ReceiveMessage(e: MessageEvent)
}

class WebsocketProxyClient extends Actor {
  import context._
  import de.bht.lischka.adminTool5k.ModelX._
  import de.bht.lischka.adminTool5k.ModelX.Picklers._
  import de.bht.lischka.adminTool5k.WebsocketProxyClient._

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
    case other => println(s"Received message ${other} while disconnected")
  }

  def closed: Receive = {
    case _ => println("Unimplemented WS close [Client]")
  }

  def reconnecting: Receive = {
    case _ => println("Unimplemented WS reconnecting [Client]")
  }

  def connected(socket: dom.WebSocket): Receive = {
    case ReceiveMessage(messageEvent: MessageEvent) => context.parent ! messageEvent.data.toString()

    case msg: String =>
      socket.send(msg)

    case ConnectionClosed => become(closed)

    case Error(e: Event) => println("Error [Client]")

    case d => println(s"Default case [Client] with ${d}")
  }
}
