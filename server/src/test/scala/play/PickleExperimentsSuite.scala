import akka.actor.{ActorRef, Props, ActorSystem, Actor}
import akka.event.LoggingReceive
import be.doeraene.spickling.PicklerRegistry
import global.PicklerSetup
import models.WSCommunicationCmds.User
import pprint.PPrint
import utest._
import scala.scalajs.js
import pprint.Config.Colors._
import be.doeraene.spickling.jsany._

object TestPickleExperimentsSuiteServer extends utest.TestSuite {
  def tests = TestSuite {

    "user is pickled and unpickled correctly"- {
      PicklerRegistry.register[User]
      val alice = User("Alice")
      val pickle: js.Any = PicklerRegistry.pickle(alice)
      assert(PicklerRegistry.unpickle(pickle) == alice)
    }
  }
}
