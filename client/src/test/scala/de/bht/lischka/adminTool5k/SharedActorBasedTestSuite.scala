import de.bht.lischka.adminTool5k.TestProbeClient
import de.bht.lischka.adminTool5k.sharedtests.{CustomTestRunner, SharedSessionSuite, AbstractTestProbe, SharedPickleSupportSuite}
import SharedPickleSupportSuite._
import SharedSessionSuite._

object SharedActorBasedTestSuite extends utest.TestSuite with CustomTestRunner {

  implicit def testProbe(): AbstractTestProbe = TestProbeClient()

  runVariousTests("Shared Actor Based Tests [Client]") (
    pickleSupportTests,
    sessionTests
  )
}
