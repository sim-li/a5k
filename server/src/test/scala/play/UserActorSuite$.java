object UserActorSuite extends utest.TestSuite  {

  /**
   * Assure the appropriate list of UserValidation errors is returned when
   * validating an user class with invalid parameters.
   *
   * We match type ATValidation[T] = ValidationNel[FailureMessage, T]
   * using scalaZ's Failure case class.
   */
  def tests = TestSuite {
    "invalid user name (invalid length and symbols ) should fail " - {
    }
  }
}
