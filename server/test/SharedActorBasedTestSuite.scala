import de.bht.lischka.adminTool5k.sharedtests.{CustomTestRunner, SharedSessionSuite, AbstractTestProbe, SharedPickleSupportSuite}
import SharedPickleSupportSuite._
import SharedSessionSuite._

object SharedActorBasedTestSuite extends utest.TestSuite with CustomTestRunner {

  implicit def testProbe(): AbstractTestProbe = new TestProbeServer()

  runVariousTests("Shared Actor Based Tests [Server]") (
    pickleSupportTests,
    sessionTests
  )
}
