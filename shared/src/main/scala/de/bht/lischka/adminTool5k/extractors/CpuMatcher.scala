package de.bht.lischka.adminTool5k.extractors

import scala.util.Try

object CpuMatcher {
  def unapply(identifierAndvalue: (String, String)): Option[Double] = identifierAndvalue match {
    case ("CPU", value) => Try(Some(value.toDouble)).getOrElse(None)
    case _ => None
  }
}
