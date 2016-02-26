package de.bht.lischka.adminTool5k.extractors

object PidMatcher {
  def unapply(identifierAndvalue: (String, String)): Option[Int] = identifierAndvalue match {
    case ("PID", value) => Option(value.toInt) match {
      case Some(v: Int) => Some(v)
      case None => None
    }
    case _ => None
  }
}
