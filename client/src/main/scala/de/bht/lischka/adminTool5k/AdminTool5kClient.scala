package de.bht.lischka.adminTool5k

import akka.actor._
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}

import scala.scalajs.js

object AdminTool5kClient extends js.JSApp {
  val system = ActorSystem("adminTool5k-ui")

  def main(): Unit = {
    val url = "ws://localhost:9000/at5k/ws"
    val socket = new dom.WebSocket(url)
    // ==> val clientProxy = system.actorOf(Props(classOf[]), "")

    socket.onopen = {
      (e: Event) =>
    }

    socket.onopen = {
      (e: Event) =>
    }

    socket.onopen = {
      (e: Event) =>
    }

    socket.onmessage = {
      (e: MessageEvent) =>
    }
  }
}
