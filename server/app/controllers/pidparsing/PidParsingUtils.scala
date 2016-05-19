package controllers.pidparsing

import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.extractors._

import scala.concurrent.duration._

object PidParsingUtils {
  val pattern = "(?m)PID.+".r

  def titleColumns(pidOutput: String): Array[String] = pattern.findFirstIn(pidOutput) match {
    case Some(l: String) => l.split(" ").filter(field => !field.isEmpty())
    case None => Array[String]()
  }

  def apply(pidOutput: String) = new PidParsingUtils(pidOutput, titleColumns(pidOutput))
}

class PidParsingUtils(pidOutput: String, titleColumnEntries: Array[String]) {
  def dataColumns(line: String): Array[String] = line.replaceAll("[ ]+", " ").trim.split(" ")

  //@TODO: Yuck! Reduce this! Using this series of copy commands can't be performant!
  def fieldsFromLine(line: String): Option[SystemStatsEntry] = {
    val fieldNamesInExpectedOrder = List("%CPU", "COMMAND", "MEM", "PID", "TIME")
    val fields: Array[(String, String)] = (titleColumnEntries zip dataColumns(line))
      .filter(c => fieldNamesInExpectedOrder.contains(c._1))
      .sorted
    val constructedLine = (new SystemStatsEntry() /: fields)((l: SystemStatsEntry, f: (String, String)) =>
      f match {
        case CpuMatcher(usage: Double) => l.copy(cpu=Some(Cpu(usage)))
        case ProcessNameMatcher(name) => l.copy(processName=Some(ProcessName(name)))
        case MemoryUsageMatcher(usage: Long) => l.copy(memoryUsage=Some(MemoryUsage(usage)))
        case PidMatcher(pid) => l.copy(pid=Some(Pid(pid)))
        case TimeMatcher(time) => l.copy(time=Some(TimeAlive(time)))
        case _ => l
      }
    )
    if (!constructedLine.pid.isDefined) {
      return None
    } else {
      return Some(constructedLine)
    }
  }

  def rows: List[SystemStatsEntry] = {
    val interestingPart: String = pidOutput.split(PidParsingUtils.pattern.toString)(1)
    (List[SystemStatsEntry]() /: interestingPart.split("\n")) {
      (buffer: List[SystemStatsEntry], line: String) =>
        println(line)
        fieldsFromLine(line) match {
          case Some(sysStatsLine: SystemStatsEntry) => sysStatsLine :: buffer
          case _ => buffer
        }
    }
  }
}

