package de.bht.lischka.adminTool5k.extractors

object MemoryUsageMatcher {
  // @TODO: Introduce type system hirarchy of matchers
  // The only values that change here are the identifiers plus the type
  def unapply(identifierAndvalue: (String, String)): Option[Double] = identifierAndvalue match {
    case ("MEM", value) => Option(value.toDouble) match {
      case Some(v: Double) => Some(v)
      case None => None
    }
    case _ => None
  }
}
