//import global.Types.ATValidation
//import models.UserValidatable
//import utest._
//import validation.{UserValidationErr, FailureMessage}
//
//import scalaz.{NonEmptyList, Failure, Success}
//
///**
// * Contains tests for class user and its validations
// */
//object UserSuite extends utest.TestSuite {
//
//  /**
//   * Assure the appropriate list of UserValidation errors is returned when
//   * validating an user class with invalid parameters.
//   *
//   * We match type ATValidation[T] = ValidationNel[FailureMessage, T]
//   * using scalaZ's Failure case class.
//   */
//  def tests = TestSuite {
//    def validateUser(user: UserValidatable, failureMatcher: (NonEmptyList[FailureMessage]) => Unit): Unit = {
//      user.validate() match {
//        case Failure(failures: NonEmptyList[FailureMessage]) =>
//          failureMatcher(failures)
//        case _ => assert(false)
//      }
//    }
//
//    "invalid user name (invalid length and symbols ) should fail " - {
//      validateUser(new UserValidatable("$"), {
//        (failures) =>
//          failures match {
//            case NonEmptyList(UserValidationErr.SPECIAL_CHARS(_), UserValidationErr.WRONG_LENGTH(_)) => assert(true)
//            case _ => assert(false)
//          }
//      })
//    }
//
//    "invalid user name (invalid length) should fail " - {
//      validateUser(new UserValidatable("a"), {
//        (failures) =>
//          failures match {
//            case NonEmptyList(UserValidationErr.WRONG_LENGTH(_)) => assert(true)
//            case _ => assert(false)
//          }
//      })
//    }
//
//    "invalid user name (invalid symbols) should fail " - {
//      validateUser(new UserValidatable("a$x??"), {
//        (failures) =>
//          failures match {
//            case NonEmptyList(UserValidationErr.SPECIAL_CHARS(_)) => assert(true)
//            case _ => assert(false)
//          }
//      })
//    }
//
//    "valid user name should pass" - {
//      val alice = new UserValidatable("alice").validate() match {
//        case Success(alice: UserValidatable) => assert(alice.name == "alice")
//        case Failure(_) => assert(false)
//      }
//    }
//
//  }
//}
