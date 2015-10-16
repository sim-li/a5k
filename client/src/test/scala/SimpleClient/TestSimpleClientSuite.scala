package SimpleClient

import utest._

object TestSimpleClientSuite extends utest.TestSuite {
    def tests = TestSuite {
      "absolutely simple test on the client side"- {
        assert(true)
      }
    }
}