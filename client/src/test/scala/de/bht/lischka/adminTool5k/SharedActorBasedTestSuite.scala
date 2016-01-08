package de.bht.lischka.adminTool5k
import akka.actor._
import de.bht.lischka.adminTool5k.InternalMessages.{PickledMessageForSending, SendMessage}
import de.bht.lischka.adminTool5k.ModelX.{TestWSMessage, WSMessage}
import de.bht.lischka.adminTool5k.pickling.{PickleSupport, Pickling, Unpickling}
import de.bht.lischka.adminTool5k.sharedtests.SharedPickleSupportSuite
import prickle.Pickle
import utest._
import utest.util.Tree

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
object SharedActorBasedTestSuite {
  import de.bht.lischka.adminTool5k.ModelX.Picklers._

  def pickleSupportTests = SharedPickleSupportSuite.pickleSupportTests(TestProbeClient)

  val pickleSupportResults = pickleSupportTests.run()

  println("Result of pickle support tests (Client)")
  println(new DefaultFormatter().format(pickleSupportTests.run()))
}
