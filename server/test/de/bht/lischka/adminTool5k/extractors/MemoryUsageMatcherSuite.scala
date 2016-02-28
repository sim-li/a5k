package de.bht.lischka.adminTool5k.extractors

import utest._

//@TODO: Fix shared tests
//@TODO: Move this to shared tests
object MemoryUsageMatcherSuite extends utest.TestSuite {
  def tests = TestSuite {
    'ParseMemoryFieldFromKBWithPlusEnding {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("1024K+"), 1024L * 1024)
    }

    'ParseMemoryFieldFromKBWithMinusEnding {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("1024K-"), 1024L * 1024)
    }

    'ParseMemoryFieldFromKB {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("1024B"), 1024L)
    }

    'ParseMemoryFieldFromKB1 {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("1024K"), 1024L * 1024)
    }

    'ParseMemoryFieldFromKB2 {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("2048K"), 2048L * 1024)
    }

    'ParseMemoryFieldFromMB {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("2048M"), 2048L * 1024 * 1024)
    }

    'ParseMemoryFieldFromG {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("2048G"), 2048L * 1024 * 1024 * 1024)
    }

    'ParseMemoryFieldFromT {
      assertMemoryUsage(MemoryUsageMatcher.memoryUsage("2048T"), 2048L * 1024 * 1024 * 1024 * 1024)
    }

    def assertMemoryUsage(usage: Option[Long], expected: Long): Unit = {
      usage match {
        case Some(u: Long) => assert(u == expected)
        case _ =>  assert(false)
      }
    }

    'MemExtractorTestNoUnit {
      ("MEM", "123") match {
        case MemoryUsageMatcher(mem: Long) => assert(mem == 123L)
        case _ => assert(false)
      }
    }

    'MemExtractorTestByte {
      assertExtractor("123B", 123L)
    }

    'MemExtractorTestKByte {
      assertExtractor("123K", 123L * 1024)
    }

    'MemExtractorTestMByte {
      assertExtractor("123M", 123L * 1024 * 1024)
    }

    //@TODO: Check if G is really the sign for Gbyte
    'MemExtractorTestGByte {
      assertExtractor("123G", 123L * 1024 * 1024 * 1024)
    }

    //@TODO: Check if T is really the sign for Tbyte
    'MemExtractorTestTByte {
      assertExtractor("123T", 123L * 1024 * 1024 * 1024 * 1024)
    }

    def assertExtractor(memoryExpressionInput: String, expectedUsage: Long) = {
      ("MEM", memoryExpressionInput) match {
        case MemoryUsageMatcher(mem: Long) => assert(mem == expectedUsage)
        case _ => assert(false)
      }
    }
  }
}
