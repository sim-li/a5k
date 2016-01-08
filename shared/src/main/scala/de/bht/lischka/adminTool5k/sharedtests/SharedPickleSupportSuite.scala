package de.bht.lischka.adminTool5k.sharedtests

import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, WSMessage}
import de.bht.lischka.adminTool5k.pickling.{PickleSupport, Pickling, Unpickling}
import prickle.Pickle
import utest._

import scala.concurrent.duration._

object SharedPickleSupportSuite {

  import de.bht.lischka.adminTool5k.ModelX.Picklers._
  import scala.concurrent.ExecutionContext.Implicits.global
  
  def pickleSupportTests() (implicit probe: AbstractTestProbe) = TestSuite {
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

      val sessionStub = system.actorOf(SessionStub.props(probe.ref()))
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