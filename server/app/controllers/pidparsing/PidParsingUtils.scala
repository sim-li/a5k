package controllers.pidparsing

import de.bht.lischka.adminTool5k.ModelX.SystemStatsLine

class PidParsingUtils(pidOutput: String) {
  val pattern = "(?m)PID.+".r

  //@TODO: Resolve this monstrous statement!
  def rows: Option[List[SystemStatsLine]] = {
    val res: List[SystemStatsLine] = (List[SystemStatsLine] /: pidOutput.tail.split("\n"))
    ((buffer: List[SystemStatsLine], line: String) => (fieldsFromLine(line) :: buffer))
  }

  def fieldsFromLine(line: String): SystemStatsLine = {
    SystemStatsLine
  }

  def parseTitleLine() = pattern.findFirstIn(pidOutput) match {
    case Some(l: String) => l.split(" ").filter(field => !field.isEmpty())
    case None => Array[String]()
  }
}
