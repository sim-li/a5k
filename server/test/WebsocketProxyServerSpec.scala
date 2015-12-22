package controllers

import akka.actor.{Props, ActorSystem}
import akka.testkit._
import controllers.proxy.WebsocketProxyServer
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, User}
import utest._

object SimpleSharedSuite extends utest.TestSuite with TestKitBase {
  implicit lazy val system = ActorSystem()

  def tests = TestSuite {
    'theLoveMeOrDie  {
      val probe = TestProbe()
      val out = TestProbe()
      val router = TestProbe()
      val websocketProxyServer = system.actorOf(Props[WebsocketProxyServer], "WebsocketProxyServer")
      probe.send(websocketProxyServer, LoginUser(User("TestUser")))
      probe.
    }
  }

  shutdown(system)
}