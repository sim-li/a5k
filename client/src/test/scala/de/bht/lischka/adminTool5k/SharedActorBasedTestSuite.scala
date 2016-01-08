package de.bht.lischka.adminTool5k
import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, WSMessage}
import de.bht.lischka.adminTool5k.pickling.{PickleSupport, Pickling, Unpickling}
import de.bht.lischka.adminTool5k.sharedtests.SharedPickleSupportSuite._
import de.bht.lischka.adminTool5k.sharedtests.SharedSessionSuite._
import de.bht.lischka.adminTool5k.sharedtests.{AbstractTestProbe, SharedPickleSupportSuite}
import prickle.Pickle
import utest._
import utest.util.Tree
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object SharedActorBasedTestSuite extends utest.TestSuite {
  println("Result of pickle support tests (Client)")

  implicit def probe: AbstractTestProbe = TestProbeClient()

  val tests = {
    pickleSupportTests()
    sessionTests()
  }
}
