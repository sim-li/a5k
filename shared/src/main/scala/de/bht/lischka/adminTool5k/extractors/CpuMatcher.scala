package de.bht.lischka.adminTool5k.extractors

object CpuMatcher {
  def unapply(identifierAndvalue: (String, String)): Option[Double] = identifierAndvalue match {
    case ("CPU", value) => Option(value.toDouble) match {
      case Some(v: Double) => Some(v)
      case None => None
    }
    case _ => None
  }
}
