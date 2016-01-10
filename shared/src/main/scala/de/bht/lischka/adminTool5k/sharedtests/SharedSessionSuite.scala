package de.bht.lischka.adminTool5k.sharedtests

import akka.actor.ActorSystem
import de.bht.lischka.adminTool5k.InternalMessages.{RegisterListener, PickledMessageForSending}
import de.bht.lischka.adminTool5k.ModelX.{LoginUser, TestWSMessage, User}
import de.bht.lischka.adminTool5k.Session
import utest._

import scala.concurrent.duration._

/**
  * Adding an extends TestKit forces us to place
  * system in the main scope, therefore all tests will
  * use one actor system which is not ideal since we want
  * a clean state for every test we run.
  */

object SharedSessionSuite {
  def sessionTests()(implicit testProbe: () => AbstractTestProbe) = TestSuite {
    'sessionTests {
      implicit val system = ActorSystem()
      val websocketOut = testProbe()
      val router = testProbe()
      val probe = testProbe()
      val session = system.actorOf(Session.props(websocketOut.ref, router.ref), "Session")

      'loggedInUserForwardsMessageToRouter {
        val testMessage = TestWSMessage("loggedInUserForwardsMessageToRouter")
        probe.send(session, LoginUser(User("TestUser")))
        probe.send(session, testMessage)
//        router.ignoreMessage { case RegisterListener => true }
        router.expectMsg(500 millis, RegisterListener)
        router.expectMsg(500 millis, testMessage)
      }

      'loggedOutUserDoesNotForwardMessageToRouter {
        val testMessage = TestWSMessage("loggedOutUserDoesNotForwardMessageToRouter")
        probe.send(session, testMessage)
        router.expectMsg(500 millis, RegisterListener)
        router.expectNoMsg(500 millis)
      }

      'loggedOutUserDoesNotForwardMessageToWebsocket {
        val testMessage = PickledMessageForSending("serializedString")
        probe.send(session, PickledMessageForSending("serializedString"))
        websocketOut.expectNoMsg(500 millis)
      }

      'loggedInUserDoesForwardMessageToWebsocket {
        val testMessage = PickledMessageForSending("serializedString")
        probe.send(session, LoginUser(User("TestUser")))
        probe.send(session, testMessage)
        websocketOut.expectMsg(500 millis, "serializedString")
      }
    }
  }
}