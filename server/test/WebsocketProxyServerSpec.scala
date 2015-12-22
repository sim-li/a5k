package controllers

import akka.actor.{Props, ActorSystem}
import akka.testkit._
import controllers.proxy.WebsocketProxyServer
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, LoginUser, User}
import utest._
import scala.concurrent.duration._

object SimpleSharedSuite extends utest.TestSuite {

  def tests = TestSuite {
    'websocketProxyTests {
      implicit val system = ActorSystem()
      val probe = TestProbe()
      val websocketOut = TestProbe()
      val router = TestProbe()
      val websocketProxyServer = system.actorOf(WebsocketProxyServer.props(websocketOut.ref, router.ref), "WebsocketProxy")
      val testMessage = TestWSMessage("Test")

      'loggedInUserForwardsMessageToRouter {
        probe.send(websocketProxyServer, LoginUser(User("TestUser")))
        probe.send(websocketProxyServer, testMessage)
        router.expectMsg(testMessage)
      }

      'loggedOutUserDoesNotForwardMessageToRouter {
        probe.send(websocketProxyServer, testMessage)
        router.expectNoMsg()
      }
    }
  }

}