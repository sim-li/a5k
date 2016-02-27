package de.bht.lischka.adminTool5k.extractors

import scala.util.Try

object PidMatcher {
  def unapply(identifierAndValue: (String, String)): Option[Int] = identifierAndValue match {
    case ("PID", value) => Try(Some(value.toInt)).getOrElse(None)
    case _ => None
  }
}
