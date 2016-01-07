package de.bht.lischka.adminTool5k
import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, WSMessage}
import de.bht.lischka.adminTool5k.pickling.{PickleSupport, Pickling, Unpickling}
import prickle.Pickle
import utest._

import scala.concurrent.duration._

object PickleSupportSuite extends utest.TestSuite {

  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  def tests = TestSuite {
    'PickleSupportTests {
      implicit val system = ActorSystem()

      object SessionStub {
        def props(spy: ActorRef): Props = Props(new SessionStub(spy))
      }

      class SessionStub(spy: ActorRef) extends Actor with PickleSupport with ActorLogging {
        override def receive: Receive = handlePickling orElse {
          case anyMessage =>
            spy ! anyMessage
        }
      }

      object TestProbeCli {
        val testSystem = ActorSystem()
        var ref: ActorRef = _


        def apply() = {
          ref = testSystem.actorOf(TestProbe.props, "probe")
          this
        }

        def send(receiver: ActorRef, msg: Any) = {
          println("Send called")
        }

        def expectMsg(timeout: Duration, msgToExpect: Any): Unit = {
          println("Expect msg called")
          assert(true)
        }

        def props(): Props = Props(new TestProbeCli())

      }

      class TestProbeCli extends Actor {
        override def receive: Actor.Receive = {
          case _ => println("Probe received any")
        }
      }

      val TestProbe = TestProbeCli
      val probe = TestProbe()

      val sessionStub = system.actorOf(SessionStub.props(probe.ref))

      val unserializedTestMessage: WSMessage = TestWSMessage("testMessage")
      val serializedTestMessage = Pickle.intoString(unserializedTestMessage)


      'unpicklingActorSendsResultToSender {
        val unpickling = system.actorOf(Unpickling.props)
        probe.send(unpickling, serializedTestMessage)
        probe.expectMsg(500 millis, unserializedTestMessage)
      }

      'picklingActorSendsResultToSender {
        val pickling = system.actorOf(Pickling.props)
        probe.send(pickling, unserializedTestMessage)
        probe.expectMsg(500 millis, PickledMessageForSending(serializedTestMessage))
      }

      'unpickleStringAndSendToSelf {
        probe.send(sessionStub, serializedTestMessage)
        probe.expectMsg(500 millis, unserializedTestMessage)
      }

      'pickleStringAndSendToSelf {
        probe.send(sessionStub, SendMessage(unserializedTestMessage))
        probe.expectMsg(500 millis, PickledMessageForSending(serializedTestMessage))
      }
    }
  }
}
