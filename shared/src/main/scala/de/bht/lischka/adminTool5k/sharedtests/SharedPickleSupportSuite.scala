package de.bht.lischka.adminTool5k.sharedtests

import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.{UnpickledMessageFromNetwork, PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, WSMessage}
import de.bht.lischka.adminTool5k.pickling.{PickleSupport, Pickling, Unpickling}
import prickle.Pickle
import utest._
import scala.concurrent.duration._

object SharedPickleSupportSuite {

  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  def pickleSupportTests()(implicit testProbe: () => AbstractTestProbe) = TestSuite {
    'PickleSupportTests {
      implicit val system = ActorSystem()
      val probe = testProbe()

      object SessionStub {
        def props(spy: ActorRef): Props = Props(new SessionStub(spy))
      }

      class SessionStub(spy: ActorRef) extends Actor with PickleSupport with ActorLogging {
        override def receive: Receive = handlePickling orElse {
          case anyMessage =>
            spy ! anyMessage
        }
      }

      val sessionStub = system.actorOf(SessionStub.props(probe.ref()))
      val deserializedMessage: WSMessage = TestWSMessage("testMessage")
      val serializedMessage = Pickle.intoString(deserializedMessage)

      'unpicklingActorSendsResultToSender {
        val unpickling = system.actorOf(Unpickling.props)
        probe.send(unpickling, serializedMessage)
        probe.expectMsg(500 millis, UnpickledMessageFromNetwork(deserializedMessage))
      }

      'picklingActorSendsResultToSender {
        val pickling = system.actorOf(Pickling.props)
        probe.send(pickling, deserializedMessage)
        probe.expectMsg(500 millis, PickledMessageForSending(serializedMessage))
      }

      'unpickleStringAndSendToSelf {
        probe.send(sessionStub, serializedMessage)
        probe.expectMsg(500 millis, UnpickledMessageFromNetwork(deserializedMessage))
      }

      'pickleStringAndSendToSelf {
        probe.send(sessionStub, SendMessage(deserializedMessage))
        probe.expectMsg(500 millis, PickledMessageForSending(serializedMessage))
      }
    }
  }
}
