package controllers

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.testkit._
import controllers.Router._
import de.bht.lischka.adminTool5k.InternalMessages.RegisterListener
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, LoginUser, User}
import de.bht.lischka.adminTool5k.Session
import utest._
import scala.concurrent.duration._

/**
  * Adding an extends TestKit forces us to place
  * system in the main scope, therefore all tests will
  * use one actor system which is not ideal since we want
  * a clean state for every test we run.
  */
object RouterSuite extends utest.TestSuite {
  def tests = TestSuite {
    'RouterTests {
      implicit val system = ActorSystem()
      val session =  TestProbe()
      val router =  system.actorOf(Router.props, "Router")
      val testMessage = TestWSMessage("Test")
      case object News

      'routerRegistersReceiver {
        session.send(router, RegisterListener(session.ref))
        session.send(router, TestListReceivers)
        session.expectMsg(500 millis, List(session.ref))
      }

      'newsToAllReceivers {
        val aSession = TestProbe()
        val anotherSession = TestProbe()
        val andJetAnotherSession = TestProbe()
        registerProbesAsReceivers(aSession, anotherSession, andJetAnotherSession)
        session.send(router, TestSendNewsToAllReceivers(News))
        expectNewsReceivedIn(aSession, anotherSession, andJetAnotherSession)
      }

      def registerProbesAsReceivers(receivers: TestProbe*) = {
        receivers.map(receiver =>
          session.send(router, RegisterListener(receiver.ref))
        )
      }

      def expectNewsReceivedIn(probes: TestProbe*) = {
        probes.map(probe =>
          probe.expectMsg(500 millis, News)
        )
      }
    }
  }
}
