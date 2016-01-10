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
    object ManualFilter {
      def props(target: ActorRef) = Props(new ManualFilter(target))
    }

    class ManualFilter(target: ActorRef) extends Actor {
      def receive = {
        case RegisterListener(x) =>
        case otherMsg =>
          target ! otherMsg
      }
    }

    'sessionTests {
      implicit val system = ActorSystem()
      val websocketOut = TestProbe()
      val router = TestProbe()
      /**
        * Does not seem to ignore msg properly
        */
      router.ignoreMsg { case RegisterListener => true }
      val probe = TestProbe()
      val manualFilterToRouter = system.actorOf(ManualFilter.props(router.ref))
      val session = system.actorOf(Session.props(websocketOut.ref, manualFilterToRouter), "Session")


      'loggedInUserForwardsMessageToRouter {
        val testMessage = TestWSMessage("loggedInUserForwardsMessageToRouter")
        probe.send(session, LoginUser(User("TestUser")))
        probe.send(session, UnpickledMessageFromNetwork(testMessage))
        router.expectMsg(500 millis, testMessage)
      }

      'loggedOutUserDoesNotForwardMessageToRouter {
        val testMessage = TestWSMessage("loggedOutUserDoesNotForwardMessageToRouter")
        probe.send(session, UnpickledMessageFromNetwork(testMessage))
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