package models

import global.Types
import global.Types._

abstract class ValidatebleModel[T] {
  def validate: ATValidation[T]
}
