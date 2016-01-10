package de.bht.lischka.adminTool5k.sharedtests

import utest._
import utest.framework.Test
import utest.util.Tree

trait CustomTestRunner {
  import utest.ExecutionContext.RunNow

  val tests = TestSuite {}

  def runVariousTests(title: String)(tests: Tree[Test]*) = {
    println("")
    println(title)
    println("===============================================")
    var success = 0
    var numberOfTests = 0
    tests.foreach(test => {
      val result = test.run()
      println(new DefaultFormatter().format(result))
      success += result.iterator.count(_.value.isSuccess)
      numberOfTests += result.iterator.length
    })
    println(s"Passed ${success}/${numberOfTests}")
    println("")
  }
}
