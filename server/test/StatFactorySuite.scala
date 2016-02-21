import controllers.pidparsing.StatFactory
import de.bht.lischka.adminTool5k.ModelX.Pid
import utest._
import controllers._

package controllers

class StatFactorySuite extends utest.TestSuite {
  def tests = TestSuite {
    'assertPidParseSuccess {
      val sf = new StatFactory()
      assert(sf.buildPid() == Pid())
    }
  }
}


