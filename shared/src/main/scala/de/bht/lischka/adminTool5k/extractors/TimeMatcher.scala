package de.bht.lischka.adminTool5k.extractors

object TimeMatcher {
  def unapply(identifierAndvalue: (String, String)): Option[String] = identifierAndvalue match {
    case ("TIME", value) => Option(value) match {
      case Some(v: String) => Some(v)
      case None => None
    }
    case _ => None
  }
}
