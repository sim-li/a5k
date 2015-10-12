//http://johnkurkowski.com/posts/accumulating-multiple-failures-in-a-ValidationNEL/
//https://www.innoq.com/de/blog/validate-your-domain-in-scala/
package models

import validation.{UserValidationErr, FailureMessage}

import scalaz.{ValidationNel, Validation}
import global.Types.ATValidation

case class UserValidatable(name: String) extends ValidatebleModel[UserValidatable]{
    def validate(): ATValidation[UserValidatable] = {
      val user = this

      import scalaz._
      import scalaz.Scalaz._

      def validNameChars(name: String): ATValidation[String] = {
        val noSpecialCharsRegex = """[a-zA-Z0-9]*"""
        if (!name.matches(noSpecialCharsRegex)) UserValidationErr.SPECIAL_CHARS("You're using special characters in the username").failureNel
          else name.successNel
      }

      def validNameLength(pw: String): ATValidation[String] = {
        if (name.length <= 1) UserValidationErr.WRONG_LENGTH("Not enough characters for a pw").failureNel
          else name.successNel
      }

      (validNameChars(user.name) |@| validNameLength(user.name)) {
        (username: String, password: String) => user
      }
    }

}
