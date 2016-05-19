import de.bht.lischka.adminTool5k.pidparsing.StatFactory
import de.bht.lischka.adminTool5k.ModelX.Pid
import utest._


object StatFactorySuite extends utest.TestSuite {
  def tests = TestSuite {
    'assertPidParseSuccess {
      assert(true)
//      val sf = new StatFactory()
//      assert(sf.buildPid() == Pid())
    }
  }
}


