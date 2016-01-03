import akka.actor.Actor.Receive
import akka.actor._
import akka.testkit.TestProbe
import controllers.Router
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers, RegisterReceiver}
import controllers.pickling.{Pickling, Unpickling, PickleSupport}
import de.bht.lischka.adminTool5k.ModelX.{WSMessage, TestWSMessage}
import prickle.Pickle
import utest._
import scala.concurrent.duration._

object PickleSupportSuite extends utest.TestSuite {

  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  def tests = TestSuite {
    'PickleSupportTests {
      implicit val system = ActorSystem()

      object SessionStub {
        def props(spy: ActorRef) = Props(new SessionStub(spy))
      }

      class SessionStub(spy: ActorRef) extends Actor with PickleSupport {
        override def receive: Receive = handlePickling orElse {
          case anyMessage =>
            println("Probe will get")
            pprint.pprintln(anyMessage)
            spy ! anyMessage
          case _ => println("Got any")
        }
      }

      val probe = TestProbe()
      val sessionStub = system.actorOf(SessionStub.props(probe.ref))
      val pickling = system.actorOf(Pickling.props)
      val unpickling = system.actorOf(Unpickling.props)
      val testMessage: WSMessage = TestWSMessage("abc")
      val str = Pickle.intoString(testMessage)

      'unpicklingActorSendsResultToSender {
        probe.send(unpickling, str)
        probe.expectMsg(500 millis, testMessage)
      }

      'forwardWsMessageToSpy {
        probe.send(sessionStub, testMessage)
        probe.expectMsg(500 millis, testMessage)
      }

      'unpickleStringAndSendToSelf {
        probe.send(sessionStub, str)
        probe.expectMsg(500 millis, testMessage)
      }
    }
  }
}
