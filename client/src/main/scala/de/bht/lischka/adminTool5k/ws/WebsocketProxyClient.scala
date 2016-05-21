package de.bht.lischka.adminTool5k.ws

import akka.actor.{ActorRef, Actor, Props}
import de.bht.lischka.adminTool5k.ws.WebsocketProxyClient._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.jquery.{jQuery => jQ}

object WebsocketProxyClient {
  def props = Props(new WebsocketProxyClient())

  case class ConnectionEstablished(socket: dom.WebSocket, requester: ActorRef)

  case object ConnectionClosed

  case class Error(e: Event)

  case class ReceiveMessage(e: MessageEvent)

  case class OpenSocket(ip: String)

  case object SocketOpen
}

class WebsocketProxyClient extends Actor {
  import context._

  def registerCallbacks(ip: String, requester: ActorRef) = {
    val url = s"ws://${ip}:9000/ws-entry"
    val socket = new dom.WebSocket(url)
    socket.onopen = { (e: Event) => self ! ConnectionEstablished(socket, requester) }
    socket.onmessage = { (e: MessageEvent) => self ! ReceiveMessage(e) }
    socket.onclose = { (e: Event) => self ! ConnectionClosed }
    socket.onerror = { (e: Event) => self ! Error(e) }
  }

  override def receive: Receive = disconnected

  def disconnected: Receive = {
    case OpenSocket(ip) => registerCallbacks(ip, sender)

    case ConnectionEstablished(socket: dom.WebSocket, requester: ActorRef) =>
      context.become(connected(socket))
      requester ! SocketOpen

    case other => println(s"Received message ${other} while disconnected")
  }

  def closed: Receive = {
    case _ => println("Unimplemented WS close [Client]")
  }

  def reconnecting: Receive = {
    case _ => println("Unimplemented WS reconnecting [Client]")
  }

  def connected(socket: dom.WebSocket): Receive = {
    case OpenSocket(ip) => sender ! SocketOpen

    case ReceiveMessage(messageEvent: MessageEvent) => context.parent ! messageEvent.data.toString()

    case msg: String =>
      socket.send(msg)

    case ConnectionClosed => become(closed)

    case Error(e: Event) => println("Error [Client]")

    case d => println(s"Default case [Client] with ${d}")
  }
}
