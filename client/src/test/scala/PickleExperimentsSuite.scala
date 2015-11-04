import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.event.LoggingReceive
import be.doeraene.spickling.PicklerRegistry
import pprint.PPrint
import utest._
import utest.framework.Result
import utest.util.Tree
import scala.concurrent.ExecutionContext
import scala.scalajs.js
import pprint.Config.Colors._
import be.doeraene.spickling.jsany._
import scala.concurrent.ExecutionContext.Implicits.global

object ClientPickleSuite extends utest.TestSuite {
  val tests = TestSuite {
    case class Message(m: Any)
    abstract class AToolMsg()
    abstract class ATReceiver() extends AToolMsg
    abstract class Model() extends AToolMsg
    abstract class Event() extends AToolMsg
    case class User(name: String) extends Model
    case class ConnectUser(user: User) extends Event
    case class DisconnectUser(user: User) extends Event
    case class ToServer(msg: AToolMsg) extends ATReceiver
    case class ToClient(msg: AToolMsg) extends ATReceiver

    "pickle and unpickle simple user case class"- {
      PicklerRegistry.register[User]
      val pickle: js.Any = PicklerRegistry.pickle(User("Alice"))
      assert(PicklerRegistry.unpickle(pickle) == User("Alice"))
      "pickle and unpickle simply nested case class" - {
        PicklerRegistry.register[ConnectUser]
        val pickle = PicklerRegistry.pickle(ConnectUser(User("Alice")))
        assert(
          PicklerRegistry.unpickle(pickle) == ConnectUser(User("Alice"))
        )
        "pickle and unpickle double nested case class" - {
          PicklerRegistry.register[ToServer]
          val pickle = PicklerRegistry.pickle(ToServer(ConnectUser(User("Alice"))))
          assert(
            PicklerRegistry.unpickle(pickle) == ToServer(ConnectUser(User("Alice")))
          )
        }
      }
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

  class SimFormatter extends Formatter {
    override def formatSingle(path: Seq[String], r: Result): String = {
      "Ha, not doing anything, but should format single"
    }

    override def format(results: Tree[Result]): String = {
      results.toSeq.map(
         res=>
           s"Name ${res.name}, value ${res.value}...."
      ).toString
    }
  }

  val formatter = new SimFormatter()
  val ixy = new DefaultFormatter()

  tests.runAsync().map {
    results =>
        println("I'm a result!")
        println(formatter.format(results))
        println("I ended.")

  }
}
