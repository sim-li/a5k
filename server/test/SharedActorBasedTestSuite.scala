import de.bht.lischka.adminTool5k.sharedtests.{AbstractTestProbe, SharedPickleSupportSuite}
import utest.DefaultFormatter
import utest._
import utest.framework.Test
import utest.util.Tree

import scala.collection.mutable.ArrayBuffer
import SharedPickleSupportSuite._

object SharedActorBasedTestSuite extends utest.TestSuite {
  println("Result of pickle support tests (Server)")

  implicit def probe: AbstractTestProbe = new TestProbeServer()

  val tests = {
    pickleSupportTests()
  }
}
