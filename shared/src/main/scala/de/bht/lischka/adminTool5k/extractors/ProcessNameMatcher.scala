package de.bht.lischka.adminTool5k.extractors

object ProcessNameMatcher {
  val CutAwayPreceedingPaths = """(?:.*\/)?([^\/]+)""".r
  def unapply(identifierAndvalue: (String, String)): Option[String] = identifierAndvalue match {
    case ("COMMAND", value) => Option(value) match {
      case Some(v: String) => v match {
        case CutAwayPreceedingPaths(lastPath: String) => Some(lastPath)
        case _ => None
      }
      case None => None
    }
    case _ => None
  }
}

