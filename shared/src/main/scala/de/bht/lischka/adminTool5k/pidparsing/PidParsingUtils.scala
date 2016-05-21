package de.bht.lischka.adminTool5k.pidparsing

import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.extractors._

import scala.concurrent.duration._

object PidParsingUtils {
  val titleLinePattern = "(?m)PID.+".r

  def apply(pidOutput: String) = new PidParsingUtils(pidOutput)
}

class PidParsingUtils(pidOutput: String) {

  def fieldsFromLine(line: String): Option[SystemStatsEntry] = {
    val columns = line.replaceAll("[ ]+", " ").trim.split(" ")
    val fields = (List("%CPU", "MEM", "PID", "TIME", "COMMAND") zip columns).sorted
    val result = (new SystemStatsEntry() /: fields)((l: SystemStatsEntry, f: (String, String)) =>
      f match {
        case CpuMatcher(usage: Double) => l.copy(cpu=Some(Cpu(usage)))
        case ProcessNameMatcher(name) => l.copy(processName=Some(ProcessName(name)))
        case PidMatcher(pid) => l.copy(pid=Some(Pid(pid)))
        case TimeMatcher(time) => l.copy(time=Some(TimeAlive(time)))
        case _ => l
      }
    )
    if (!result.pid.isDefined) {
      return None
    } else {
      return Some(result)
    }
  }

  def rows: List[SystemStatsEntry] = {
    val outputBody: String = pidOutput.split(PidParsingUtils.titleLinePattern.toString)(1)
    val result = (List[SystemStatsEntry]() /: outputBody.split("\n")) {
      (buffer: List[SystemStatsEntry], line: String) =>
        fieldsFromLine(line) match {
          case Some(sysStatsLine: SystemStatsEntry) => sysStatsLine :: buffer
          case _ => buffer
        }
    }
    result
  }
}

