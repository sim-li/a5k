package controllers.pidparsing

class PidParsingUtils {
  val pattern = "(?m)PID.+".r

  def getRows(pidOutput: String): Unit = {
    for (row <- pidOutput.split("\n")) {

    }
  }

  def parseTitleLine(pidOutput: String) = pattern.findFirstIn(pidOutput) match {
    case Some(l: String) => l.split(" ").filter(field => !field.isEmpty())
    case None => Array[String]()
  }
}
