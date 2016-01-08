import de.bht.lischka.adminTool5k.sharedtests.{SharedSessionSuite, AbstractTestProbe, SharedPickleSupportSuite}
import utest.DefaultFormatter
import utest._
import utest.framework.Test
import utest.util.Tree
import scala.collection.mutable.ArrayBuffer
import SharedPickleSupportSuite._
import SharedSessionSuite._

object SharedActorBasedTestSuite extends utest.TestSuite {
  println("Result of pickle support tests (Server)")

  implicit def testProbe(): AbstractTestProbe = {
    new TestProbeServer()
  }

  val tests = {
    pickleSupportTests()
    sessionTests()
  }
}
