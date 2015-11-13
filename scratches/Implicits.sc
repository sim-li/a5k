import java.math.BigInteger

/*
 *  These operate on global class scope
 */
case class Name(first: String, last: String)
case class Person(name: Name, age: Integer)

// Explicit type specification in parameter,
// Scala does conversion
implicit def stringToName(name: String) = {
  val names = name.split(" ")
  Name(names(0), names(1))
}

Person("Alice Anderson", 31)
Person("Bob Bucherson", 32)

// Only explicit specification of type will force
// compiler to implictly convert.
val x1: Name = "Frank Hutch"
// Doesn't convert
val x2 = "Frank Hutch"

/*
 *  Implicitly convert function parameters
 */
 def printMyNumberAsNumeric[T]
    (someValue: T) (implicit multi: Numeric[T]) = {
   println(multi)
 }

printMyNumberAsNumeric(3)


