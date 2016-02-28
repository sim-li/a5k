package de.bht.lischka.adminTool5k.extractors

import utest._

object CpuMatcherSuite extends utest.TestSuite {
  def tests = TestSuite {
    'ExtractorTestPositive {
      ("CPU", "123.123") match {
        case CpuMatcher(usage: Double) => assert(usage == 123.123)
        case _ => assert(false)
      }
    }

    'ExtractorTestNegative1 {
      ("XYZ", "123.123") match {
        case CpuMatcher(usage: Double) => assert(false)
        case _ => assert(true)
      }
    }

    'ExtractorTestNegative2 {
      ("CPU", "notANumber") match {
        case CpuMatcher(usage: Double) => assert(false)
        case _ =>  assert(true)
      }
    }
  }
}
