package de.bht.lischka.adminTool5k.extractors

object ProcessNameMatcher {
  def unapply(identifierAndvalue: (String, String)): Option[String] = identifierAndvalue match {
    case ("COMMAND", value) => Option(value) match {
      case Some(v: String) => Some(v)
      case None => None
    }
    case _ => None
  }
}

