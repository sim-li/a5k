package de.bht.lischka.adminTool5k.sharedtests

import akka.actor.{ActorRef, Props, Actor, ActorSystem}
import akka.testkit.TestProbe
import de.bht.lischka.adminTool5k.InternalMessages.{UnpickledMessageFromNetwork, RegisterListener, PickledMessageForSending}
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
      val websocketOut = TestProbe()
      val router = TestProbe()
      //router.ignoreMsg { case RegisterListener(_) => true }
      val probe = TestProbe()
      val session = system.actorOf(Session.props(websocketOut.ref, router.ref), "Session")

// @TODO: Fix failing test: expected TestWSMessage(loggedInUserForwardsMessageToRouter), found RegisterListener(Actor[akka://default/user/Session#-530214830])
//      'loggedInUserForwardsMessageToRouter {
//        val testMessage = TestWSMessage("loggedInUserForwardsMessageToRouter")
//        probe.send(session, LoginUser(User("TestUser")))
//        probe.send(session, UnpickledMessageFromNetwork(testMessage))
//        router.expectMsg(500 millis, testMessage)
//      }

// @TODO: Fix failing test: received unexpected message RegisterListener(Actor[akka://default/user/Session#835510315])
//      'loggedOutUserDoesNotForwardMessageToRouter {
//        val testMessage = TestWSMessage("loggedOutUserDoesNotForwardMessageToRouter")
//        probe.send(session, UnpickledMessageFromNetwork(testMessage))
//        router.expectNoMsg(500 millis)
//      }

// @TODO: assertion failed: received unexpected message serializedString
//      'loggedOutUserDoesNotForwardMessageToWebsocket {
//        val testMessage = PickledMessageForSending("serializedString")
//        probe.send(session, PickledMessageForSending("serializedString"))
//        websocketOut.expectNoMsg(500 millis)
//      }

      'loggedInUserDoesForwardMessageToWebsocket {
        val testMessage = PickledMessageForSending("serializedString")
        probe.send(session, LoginUser(User(Some("TestUser"))))
        probe.send(session, testMessage)
        websocketOut.expectMsg(500 millis, "serializedString")
      }
    }
  }
}