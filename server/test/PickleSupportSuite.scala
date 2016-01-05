import akka.actor.Actor.Receive
import akka.actor._
import akka.testkit.TestProbe
import controllers.Router
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers, RegisterReceiver}
import controllers.pickling.{Pickling, Unpickling, PickleSupport}
import de.bht.lischka.adminTool5k.InternalMessages.PickledMessageForSending
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


      'unpicklingActorSendsResultToSender {
        val unpickling = system.actorOf(Unpickling.props)
        val testMessage: WSMessage = TestWSMessage("unpicklingActorSendsResultToSender")
        probe.send(unpickling, Pickle.intoString(testMessage))
        probe.expectMsg(500 millis, testMessage)
      }

      'picklingActorSendsResultToSender {
        import de.bht.lischka.adminTool5k.ModelX.Picklers._
        val pickling = system.actorOf(Pickling.props)
        val testMessage: WSMessage = TestWSMessage("picklingActorSendsResultToSender")
        probe.send(pickling, testMessage)
        probe.expectMsg(500 millis, PickledMessageForSending(Pickle.intoString(testMessage)))
      }

      'forwardWsMessageToSpy {
        val testMessage: WSMessage = TestWSMessage("forwardWsMessageToSpy")
        probe.send(sessionStub, testMessage)
        probe.expectMsg(500 millis, testMessage)
      }

      'unpickleStringAndSendToSelf {
        val testMessage: WSMessage = TestWSMessage("unpickleStringAndSendToSelf")
        probe.send(sessionStub, Pickle.intoString(testMessage))
        probe.expectMsg(500 millis, testMessage)
      }
    }
  }
}
