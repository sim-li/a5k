import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.event.LoggingReceive
import be.doeraene.spickling.PicklerRegistry
import global.PicklerSetup
import pprint.PPrint
import utest._
import scala.scalajs.js
import pprint.Config.Colors._
import be.doeraene.spickling.jsany._

object TestPickleExperimentsSuite extends utest.TestSuite {
  def tests = TestSuite {
    case class Message(m: Any)
    abstract class AToolMsg()
    abstract class ATReceiver()
    abstract class Model() extends AToolMsg
    abstract class Event() extends AToolMsg
    case class User(name: String) extends Model
    case class ConnectUser(user: User) extends Event
    case class DisconnectUser(user: User) extends Event
    case class ToServer(msg: AToolMsg) extends ATReceiver
    case class ToClient(msg: AToolMsg) extends ATReceiver

    "user is pickled and unpickled correctly"- {
      PicklerRegistry.register[User]
      val alice = User("Alice")
      val pickle: js.Any = PicklerRegistry.pickle(alice)
      assert(PicklerRegistry.unpickle(pickle) == alice)
    }
    "pickle and unpickle class hirarchy" - {
      PicklerRegistry.register[User]
      PicklerRegistry.register[ConnectUser]
      val bob = User("Bob")
      val pBob = PicklerRegistry.pickle(bob)
      val pConnectUserEvent = PicklerRegistry.pickle(ConnectUser(bob))
      assert(
        PicklerRegistry.unpickle(pBob) == bob,
        PicklerRegistry.unpickle(pConnectUserEvent) == ConnectUser(bob)
      )
    }

//    "actor pickling"- {
//      import pprint.pprintln
//      PicklerRegistry.register[User]
//      PicklerRegistry.register[ConnectUser]
//
//      case class IncomingMessage(pickle: Any)
//      case class OutgoingMessage(msg: AToolMsg)
//
//      class GUI(tc: ActorRef) extends Actor {
//        def receive = LoggingReceive {
//          case c: ConnectUser =>
//            tc ! c
//        }
//      }
//
//      class TechnicsClient extends Actor {
//        def receive = LoggingReceive {
//          case msg: AToolMsg  =>
//            ToServer(msg)
//        }
//      }
//
//      class TechnicsServer extends Actor {
//        def receive =  LoggingReceive {
//          case a =>
//            self ! PicklerRegistry.unpickle(a)
//          case ConnectUser(u: User) =>
//            println(s"Got user ${u}")
//        }
//      }
//      val system = ActorSystem("picklera-test-system")
////      val wsClientConnection = system.actorOf(Props[PickleReceive])
//
//
//      val bob = User("Bob")
//      val pickledConnectUser = PicklerRegistry.pickle(bob)
//    }

  }
}
