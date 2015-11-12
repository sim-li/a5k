package de.bht.lischka.adminTool5k
import akka.actor._
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}
import scala.scalajs.js

object AdminTool5kClient extends js.JSApp {
  val system = ActorSystem("adminTool5k-ui")

  def main(): Unit = {
    println("ScalaJS is here, my friend")

    val url = "ws://localhost:9000/at5k/ws"
    val socket = new dom.WebSocket(url)

    socket.onopen = {
      (e: Event) =>
        println("Wow, it opened :: the Websocket")
    }

    socket.onmessage = {
      (e: MessageEvent) =>
        println("Oh wow, there's a message :: the Websocket")
    }

    socket.onclose = {
      (e: Event) =>
        println("Oh no, it closed :: the Websocket")
    }

    socket.onerror = {
      (e: Event) =>
        println("Oh no, got an error :: the Websocket")
    }

  }
}
