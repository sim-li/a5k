import akka.actor.Actor.Receive
import akka.actor._
import akka.testkit.TestProbe
import controllers.Router
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers, RegisterReceiver}
import controllers.pickling.{Pickling, Unpickling, PickleSupport}
import de.bht.lischka.adminTool5k.InternalMessages.{SendMessage, PickledMessageForSending}
import de.bht.lischka.adminTool5k.ModelX.{WSMessage, TestWSMessage}
import prickle.{Unpickle, Pickle}
import utest._
import scala.concurrent.duration._
import scala.util.Success

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
