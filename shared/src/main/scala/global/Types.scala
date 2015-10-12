package global

import validation.FailureMessage

import scalaz.ValidationNel

object Types {
  type ATValidation[T] = ValidationNel[FailureMessage, T]
}