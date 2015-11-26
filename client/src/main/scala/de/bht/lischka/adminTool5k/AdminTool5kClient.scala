package de.bht.lischka.adminTool5k

import akka.actor.Actor.Receive
import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX.WSMessage
import de.bht.lischka.adminTool5k.WebsocketProxyClient.{Error, ConnectionClosed, UnpickleRawMessage}
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}
import scala.scalajs.js
import prickle._

import scala.util.{Failure, Success, Try}

object WebsocketProxyClient {
  def props: Props = Props(new WebsocketProxyClient())

  case class ConnectionEstablished(socket: dom.WebSocket)

  case object ConnectionClosed

  case class Error(e: Event)

  case class UnpickleRawMessage(e: MessageEvent)
}

class WebsocketProxyClient extends Actor {
  import context._
  import de.bht.lischka.adminTool5k.ModelX._
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  val RECONNECTING_UNIMPLIMENTED = "Unimplemented WS reconnecting [Client]"
  val CLOSED_UNIMPLEMENTED = "Unimplemented WS close [Client]"
  val GOT_UNPICKLED_MSG = "I got unpickled MSG [Client]"
  val DEFAULT_CASE = "Default case triggered [Client]"
  val GOT_ERROR = "Got an error, babe [Client]"
  val AM_CONNECTED = "I am connected [Client]"

  def closed: Receive = {
    case _ => println(CLOSED_UNIMPLEMENTED)
  }

  def reconnecting: Receive = {
    case _ => println(RECONNECTING_UNIMPLIMENTED)
  }

  def connected(socket: dom.WebSocket): Receive = {
    case UnpickleRawMessage(e) =>
      println("Unpickling")
      println(e.data)
      println(e.data.toString())
      Unpickle[WSMessage].fromString(e.data.toString()) match {
        case Success(m: WSMessage) =>
          println("Unpickled successfully, now sending.")
          self ! m
        case Failure(f) =>
          println("Got failure unpickling WS Message [Client]")
          println(s"Failure is ${f}")
      }

    case unpickledMessage: WSMessage => println(s"${GOT_UNPICKLED_MSG} ${unpickledMessage}")

    case SendMessage(msg: WSMessage) => socket.send(Pickle.intoString(msg))

    case ConnectionClosed => become(closed)

    case Error(e: Event) => println(GOT_ERROR)

    case _ => println(DEFAULT_CASE)
  }

  def disconnected: Receive = {
    case socket: dom.WebSocket =>
      println(AM_CONNECTED)
      context.become(connected(socket))
      // Context may not be ready jet
      println("Send out a Hello to Server")
      self ! SendMessage(Hello("client"))
  }

  override def receive: Receive = disconnected
}

object AdminTool5kClient extends js.JSApp {
  import ModelX._
  import ModelX.Picklers._

  def main(): Unit = {
    println("Client up again")
    val url = "ws://localhost:9000/ws-entry"
    val socket = new dom.WebSocket(url)
    val system = ActorSystem("adminTool5k-client")
    val websocketProxy = system.actorOf(Props[WebsocketProxyClient], "websocketProxyClient")
    socket.onopen = { (e: Event) => websocketProxy ! socket } // Wrap Socket?
    socket.onmessage = { (e: MessageEvent) => websocketProxy ! WebsocketProxyClient.UnpickleRawMessage(e) }
    socket.onclose = { (e: Event) => websocketProxy ! WebsocketProxyClient.ConnectionClosed }
    socket.onerror = { (e: Event) => WebsocketProxyClient.Error(e) }
  }
}
