package de.bht.lischka.adminTool5k.extractors

import utest._

object ProcessNameMatcherSuite extends utest.TestSuite {
  def tests = TestSuite {
    'ExtractorTestPositive {
      ("COMMAND", "TestApp") match {
        case ProcessNameMatcher(name: String) => assert(name == "TestApp")
        case _ => assert(false)
      }
    }

    'ExtractorTestNegative {
      ("XYZ", "TestApp") match {
        case ProcessNameMatcher(name: String) => assert(false)
        case _ => assert(true)
      }
    }
  }
}
