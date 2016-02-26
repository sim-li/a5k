package controllers.pidparsing

import de.bht.lischka.adminTool5k.ModelX._
import de.bht.lischka.adminTool5k.extractors.PidMatcher

object PidParsingUtils {
  val pattern = "(?m)PID.+".r

  def titleColumns(pidOutput: String): Array[String] = pattern.findFirstIn(pidOutput) match {
    case Some(l: String) => l.split(" ").filter(field => !field.isEmpty())
    case None => Array[String]()
  }

  def apply(pidOutput: String) =
    new PidParsingUtils(pidOutput, titleColumns(pidOutput))

}


class PidParsingUtils(pidOutput: String, titleColumnEntries: Array[String]) {

  def dataColumns(line: String): Array[String] = line.replaceAll("[ ]+", " ").trim.split(" ")

  def fieldsFromLine(line: String): Option[SystemStatsLine] = {
    val dataColumnEntries: Array[String] = dataColumns(line)
    if (dataColumnEntries.length != titleColumnEntries.length) {
      return None
    }
    val columnsForOneLine: Array[(String, String)] = titleColumnEntries zip dataColumnEntries
    var pid: Option[Pid] = None
    for (column <- columnsForOneLine) {
      column match {
        case PidMatcher(id) => pid = Some(Pid(id))
      }
    }
    if (!pid) {
      return None
    }
    for (column <- columnsForOneLine) {
      column match {
      }
    }
    SystemStatsLine(Pid(7515), Some(ProcessName("top")), Some(Cpu(2.6)), Some(Time("00:04.27")), Some(MemoryUsage(2220032)))
  }

  def memoryUsage(memColumn: String): MemoryUsage = {
    val Regex = """([0-9]+)([KkMmBbGgTt])[+-]*""".r
    val Regex(mem: String, unit: String) = memColumn
    val multiplicator = unit match {
      case "B" => 1.0
      case "K" => 1024.0
      case "M" => 1024.0 * 1024
      case "G" => 1024.0 * 1024 * 1024
      case "T" => 1024.0 * 1024 * 1024 * 1024 //@TODO: CHECK if G and T symbol are really commmon in top
    }
    MemoryUsage(mem.toDouble * multiplicator)
  }
}

