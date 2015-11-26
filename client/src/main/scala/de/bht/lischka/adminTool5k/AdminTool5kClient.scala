package de.bht.lischka.adminTool5k
import akka.actor._
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}
import scala.scalajs.js
import prickle._

object AdminTool5kClient extends js.JSApp {
  import ModelX._
  import ModelX.Picklers._

  val system = ActorSystem("adminTool5k-ui")

  def main(): Unit = {
    val url = "ws://localhost:9000/ws-entry"
    val socket = new dom.WebSocket(url)

    socket.onopen = {
      (e: Event) =>
        /**
          *  Has to happen! Specify super type!Auto type determination doesn't work!
          */
        def user: WSMessage = User("Test User")
        def pickle =  Pickle.intoString(user)
        println("Pickle is")
        println(pickle)
        socket.send(pickle)
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
