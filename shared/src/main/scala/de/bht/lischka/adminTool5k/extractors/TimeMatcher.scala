package de.bht.lischka.adminTool5k.extractors

import scala.concurrent.duration._
import scala.util.Try

object TimeMatcher {
  def convertTopTimeToDuration(topTimeExpr: String): Option[Duration] = {
    val TopDuration = """([0-9]?[0-9]):([0-9]?[0-9]).([0-9]?[0-9])""".r
    topTimeExpr match {
      case TopDuration(minutes, seconds, hundredthOfSeconds) =>
        parseTimeStampTruple(minutes, seconds, hundredthOfSeconds) match {
          case (Some(m: Int), Some(s: Int), Some(ms: Int)) => Some(Duration(m + s + ms, MILLISECONDS))
          case _ => None
        }
      case _ =>  None
    }
  }

  def parseTimeStampTruple(minutes: String, seconds: String, hundrethseconds: String) = {
    (
      tryToOption({ () => minutes.toInt * 60 * 1000 }),
      tryToOption({ () => seconds.toInt * 1000 }),
      tryToOption({ () => hundrethseconds.toInt * 10 })
    )
  }

  def tryToOption(functionWithException: () => Any): Option[Any] = {
    Try(Some(functionWithException())).getOrElse(None)
  }

  def unapply(identifierAndValue: (String, String)): Option[String] = identifierAndValue match {
    case ("TIME", value) => Option(value) match {
      case Some(v: String) => Some(v)
      case None => None
    }
    case _ => None
  }
}
