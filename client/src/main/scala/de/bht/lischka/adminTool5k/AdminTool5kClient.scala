package de.bht.lischka.adminTool5k

import java.util.Date

import akka.actor.Actor.Receive
import akka.actor._
import akka.event.Logging
import de.bht.lischka.adminTool5k.InternalMessages.SendMessage
import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.WebsocketProxyClient.{Error, ConnectionClosed}
import org.scalajs.dom
import org.scalajs.dom.{Event, MessageEvent}
import scala.scalajs.js
import prickle._
import scala.util.{Failure, Success, Try}
import org.scalajs.jquery.{jQuery => jQ, _}


object AdminTool5kClient extends js.JSApp {
  import ModelX._

  def main(): Unit = {
    val system = ActorSystem("adminTool5k-client")
    val router = system.actorOf(Props[Router], "router")
    val mainScreen = system.actorOf(Props(classOf[MainScreen], router), "mainScreen")
    val loginScreen = system.actorOf(Props(classOf[LoginScreen], router), "loginScreen")
  }
}
