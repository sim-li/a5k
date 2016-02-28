package de.bht.lischka.adminTool5k.extractors

import utest._

import scala.concurrent.duration._

object TimeMatcherSuite extends utest.TestSuite {
  def tests = TestSuite {
    'TopTimeToScalaTimeConversionPositiveTest {
      assert(
        TimeMatcher.convertTopTimeToDuration("22:00.00") ==  Some(Duration(1320000, MILLISECONDS)),
        TimeMatcher.convertTopTimeToDuration("00:22.00") ==  Some(Duration(22000, MILLISECONDS)),
        TimeMatcher.convertTopTimeToDuration("00:00.22") ==  Some(Duration(220, MILLISECONDS)),
        TimeMatcher.convertTopTimeToDuration("03:05.22") ==  Some(Duration(185220, MILLISECONDS))
      )
    }

    'TopTimeToScalaTimeConversionNegativeTest {
      assert(
        TimeMatcher.convertTopTimeToDuration("22:00") ==  None,
        TimeMatcher.convertTopTimeToDuration("120:22.00") ==  None,
        TimeMatcher.convertTopTimeToDuration("22:200.00") ==  None,
        TimeMatcher.convertTopTimeToDuration("22.00") ==  None,
        TimeMatcher.convertTopTimeToDuration("2X:Y0") ==  None
      )
    }

    'StringTimeStampToIntTimeStampPositiveTest {
      assert(
        TimeMatcher.parseTimeStampTruple("11", "12", "13") == (Some(11 * 60 * 1000), Some(12 * 1000), Some(13 * 10)),
        TimeMatcher.parseTimeStampTruple("11", "XX", "13") == (Some(11 * 60 * 1000), None, Some(13 * 10)),
        TimeMatcher.parseTimeStampTruple("11", "12", "XX") == (Some(11 * 60 * 1000), Some(12 * 1000), None),
        TimeMatcher.parseTimeStampTruple("XX", "12", "13") == (None, Some(12 * 1000), Some(13 * 10)),
        TimeMatcher.parseTimeStampTruple("XX", "XX", "XX") == (None, None, None)
      )
    }

    'TryToOptionTest {
      assert(
        TimeMatcher.tryToOption { () => 100.toInt }  == Some(100),
        TimeMatcher.tryToOption { () => "abc".toInt } == None
      )
    }


//    'ExtractorTestPositive {
//      ("TIME", "TestApp") match {
//        case ProcessNameMatcher(name: String) => assert(name == "TestApp")
//        case _ => assert(false)
//      }
//    }
//
//    'ExtractorTestNegative {
//      ("XYZ", "TestApp") match {
//        case ProcessNameMatcher(name: String) => assert(false)
//        case _ => assert(true)
//      }
//    }
  }
}
