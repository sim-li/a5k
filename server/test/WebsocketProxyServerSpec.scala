package controllers

import akka.actor.{Props, ActorSystem}
import akka.testkit._
import controllers.proxy.WebsocketProxyServer
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, LoginUser, User}
import utest._
import scala.concurrent.duration._

/**
  * Adding an extends TestKit forces us to place
  * system in the main scope, therefore all tests will
  * use one actor system which is not ideal since we want
  * a clean state for every test we run.
  */
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
        router.expectMsg(500 millis, testMessage)
      }

      'loggedOutUserDoesNotForwardMessageToRouter {
        probe.send(websocketProxyServer, testMessage)
        router.expectNoMsg(500 millis)
      }
    }
  }

}