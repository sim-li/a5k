package controllers

import java.util.Date

import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.extractors.PidMatcher
import utest._
import controllers.pidparsing.PidParsingUtils

object PidParserSuite extends utest.TestSuite {
  val pidFakeInput = """
    Processes: 250 total, 2 running, 8 stuck, 240 sleeping, 1188 threads   04:41:47
    Load Avg: 2.04, 2.39, 1.90  CPU usage: 6.3% user, 5.55% sys, 88.40% idle
    SharedLibs: 101M resident, 14M data, 7140K linkedit.
    MemRegions: 47528 total, 1192M resident, 22M private, 343M shared.
    PhysMem: 4078M used (1154M wired), 16M unused.
    VM: 697G vsize, 529M framework vsize, 1051203(0) swapins, 1292216(0) swapouts.
    Networks: packets: 3633201/4830M in, 2020206/203M out.
    Disks: 1333452/28G read, 506568/22G written.

    PID   COMMAND      %CPU  TIME     #TH   #WQ  #PORT MEM    PURG   CMPRS  PGRP
    7515  top          2.6   00:04.27 1/1   0    21    2168K  0B     228K   7515
    7511  bash         0.0   00:00.02 1     0    17    44K    0B     632K   7511
    7510  login        0.0   00:00.19 2     0    28    508K   0B     836K   7510
    7499  fsnotifier   0.0   00:00.02 3     0    30    224K   0B     584K   7495
    7495  idea         0.7   03:27.12 50    1    314   287M+  0B     412M-  7495
    7480  smd          0.0   00:00.01 2     1    21    8192B  0B     788K   7480
    7469  ocspd        0.0   00:00.05 2     0    33    40K    0B     1472K  7469
    7468  com.apple.iC 0.0   00:00.19 2     0    50    1308K  0B     948K   7468
    7440  Skype        0.0   00:53.99 44    4    467   25M    0B     141M   7440
    7420  nsurlstorage 0.0   00:00.03 2     0    33    16K    0B     1624K  7420
    7376  aslmanager   0.0   00:00.01 2     1    27    16K    0B     884K   7376
    7332  suhelperd    0.0   00:00.04 2     0    36    16K    0B     1036K  7332
    7331  mapspushd    0.0   00:00.34 4     0    107   24K    0B     3360K  7331
    7330  softwareupda 0.0   00:01.99 2     0    63    16K    0B     31M    7330
    7329  PrintUITool  0.0   00:00.09 2     0    41    24K    0B     4616K  7329
    7269  plugin-conta 0.8   02:53.00 20    0    274   12M+   0B     70M-   3456
    7248  Spotify Help 0.0   00:54.97 12    0    103   43M    0B     61M    7237
    7240  VTDecoderXPC 0.0   00:00.03 2     0    38    24K    0B     1248K  7240
    7238  Spotify Help 0.0   00:15.03 4     0    94    956K   0B     36M    7237
    7237  Spotify      0.0   01:03.25 34    0    445   9112K  0B     51M    7237
  """"

  val utils = PidParsingUtils(pidFakeInput)

  def tests = TestSuite {
    'PidParserTest {
      'PidParsingUtilsTest {
        def assertColumnTitles(actual: Array[String]) = {
          val expected = Array("PID", "COMMAND", "%CPU", "TIME", "#TH", "#WQ", "#PORT", "MEM", "PURG", "CMPRS", "PGRP")
          assertArraysEqual(actual, expected)
        }

        def stringFromOptionEqOtherString(string1: Option[String], string2: String): Boolean = {
          string1 match {
            case Some(s) => s == string2
            case None => false
          }
        }

        'ParseNormalLineTest {
          assertArraysEqual(
            utils.dataColumns("7515  top          2.6   00:04.27 1/1   0    21    2168K  0B     228K   7515")
            ,Array[String]("7515", "top", "2.6", "00:04.27", "1/1", "0", "21", "2168K", "0B", "228K", "7515")
          )
        }

        'ParseTitleLineTest {
          assertColumnTitles(PidParsingUtils.titleColumns(pidFakeInput))
        }

        def assertMemoryUsage(usage: MemoryUsage, expected: MemoryUsage): Unit = {
          usage match {
            case MemoryUsage(u: Double) =>
              assert(u == expected.usage)
            case _ =>
              assert(false)
          }
        }

        'ParseMemoryFieldFromKBWithPlusEnding {
          assertMemoryUsage(utils.memoryUsage("1024K+"), MemoryUsage(1024.0 * 1024))
        }

        'ParseMemoryFieldFromKBWithMinusEnding {
          assertMemoryUsage(utils.memoryUsage("1024K-"), MemoryUsage(1024.0 * 1024))
        }

        'ParseMemoryFieldFromKB {
          assertMemoryUsage(utils.memoryUsage("1024B"), MemoryUsage(1024.0))
        }

        'ParseMemoryFieldFromKB1 {
          assertMemoryUsage(utils.memoryUsage("1024K"), MemoryUsage(1024.0 * 1024))
        }

        'ParseMemoryFieldFromKB2 {
          assertMemoryUsage(utils.memoryUsage("2048K"), MemoryUsage(2048.0 * 1024))
        }


        'ParseMemoryFieldFromMB {
          assertMemoryUsage(utils.memoryUsage("2048M"), MemoryUsage(2048.0 * 1024 * 1024))
        }

        'ParseMemoryFieldFromG {
          assertMemoryUsage(utils.memoryUsage("2048G"), MemoryUsage(2048.0 * 1024 * 1024 * 1024))
        }

        'ParseMemoryFieldFromT {
          assertMemoryUsage(utils.memoryUsage("2048T"), MemoryUsage(2048.0 * 1024 * 1024 * 1024 * 1024))
        }

        'ExtractorExperimentTest {
          val pid: Option[Pid] = ("PID", "123") match {
            case PidMatcher(id) => Some(Pid(id))
            case _ => None
          }
          pid match {
            case Some(pid: Pid) => assert(pid.pid == 123)
            case _ => assert(false)
          }
        }

//        'FieldsFromCompleteColumn1 {
//          val correctlyFormattedLine = """
//          7515  top          2.6   00:04.27 1/1   0    21    2168K  0B     228K   7515
//          """
//          val utils = new PidParsingUtils(pidFakeInput)
//          assertSystemStatsLinesEqual(
//            utils.fieldsFromLine(correctlyFormattedLine),
//            SystemStatsLine(
//              pid = Pid(7515),
//              processName = Some(ProcessName("top")),
//              cpu = Some(Cpu(2.6)),
//              time = Some(Time("00:04.27")),
//              memoryUsage = Some(MemoryUsage(2220032))
//            )
//          )
//        }

//        'FieldsFromCompleteColumn2 {
//          val correctlyFormattedLine = """
//          7269  plugin-conta 0.8   02:53.00 20    0    274   12M+   0B     70M-   3456
//          """
//          val utils = new PidParsingUtils(pidFakeInput)
//          assertSystemStatsLinesEqual(
//            utils.fieldsFromLine(correctlyFormattedLine),
//            SystemStatsLine(
//              pid = Pid(7269),
//              processName = Some(ProcessName("plugin-conta")),
//              cpu = Some(Cpu(0.8)),
//              time = Some(Time("02:53.00")),
//              memoryUsage = Some(MemoryUsage(12 * 1024 * 1024))
//            )
//          )
//        }
//        'GetRowsTest {
//          val input =
//            """
//              7515  top          2.6   00:04.27 1/1   0    21    2168K  0B     228K   7515
//              7511  bash         0.0   00:00.02 1     0    17    44K    0B     632K   7511
//            """
//          val expected = List(
//            SystemStatsLine(
//              Pid(7515),
//              Some("top"),
//              Some(Cpu(2.6)),
//              Some(Time(new Date())),
//              Some(MemoryUsage(2168))   //@TODO: Convert if other unit than K, write testcase for "M"
//            ),
//            SystemStatsLine(
//              Pid(7511),
//              Some("bash"),
//              Some(Cpu(0.0)),
//              Some(Time(new Date())),
//              Some(MemoryUsage(44)
//              )
//            ))
//          val actual = new PidParsingUtils(pidFakeInput).rows match {
//            case Some(res) => assert(res == expected)
//            case None => assert(false)
//          }
//
//        }
        //@TODO: Write assertion that compares this list to another list of SystemStatsLines containing the result!
        //@TODO: Check for correct format
        //@TODO: Check for right ammount of fields, or implement some more intelligent field parsing algorithm.
      }
    }
  }

  def assertSystemStatsLinesEqual(actual: SystemStatsLine, expected: SystemStatsLine): Unit = {
    assert(
      expected.pid == actual.pid,
      expected.processName == actual.processName,
      expected.cpu == actual.cpu,
      expected.time == actual.time,
      expected.memoryUsage == actual.memoryUsage
    )
  }

  def assertArraysEqual(a: Array[String], b: Array[String]): Unit = {
    for (i <- 0 to a.length - 1) {
      val aI = a(i)
      val bI = b(i)
      if (!a(i).contentEquals(b(i))) {
      println(s"Strings not equal: A is <${aI}> and B is <${bI}>")
        assert(false)
      } else {
        assert(true)
      }
    }
  }
}