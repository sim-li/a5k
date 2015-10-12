package validation

trait FailureMessage {
  val msg: String
}

object UserValidationErr {
  case class SPECIAL_CHARS(msg: String) extends FailureMessage
  case class WRONG_LENGTH(msg: String) extends FailureMessage
}