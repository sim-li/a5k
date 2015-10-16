import utest._

object TestSimpleServerSuite extends utest.TestSuite {
  def tests = TestSuite {
    "absolutely simple test on the server side"- {
      assert(true)
    }
  }
}
