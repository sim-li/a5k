(1 to 100).map {
  i =>
    if (i % 3 == 0) "Fizz"
    else if (i % 5 == 0) "Buzz"
    else if (i % 3 == 0 && i % 5 == 0) "FizzBuzz"
    else i
}

(1 to 100).map {
  case i if i % 3 == 0 && i % 5 == 0 => "Fizzbuzz"
  case i if i % 3 == 0 => "Fizz"
  case i if i % 5 == 0 => "Buzz"
  case i => i
}

(1 to 100).map {
  n =>
    (n % 3, n % 5) match {
      case (0, 0) => "Fizzbuzz"
      case (0, _) => "Fizz"
      case (_, 0) => "Buzz"
      case _ => n
    }
}

case class FizzBuzzer(i: Integer, s: String = "")

def fizz: Int => FizzBuzzer = i =>
  if (i % 3 == 0) FizzBuzzer(i, "Fizz") else FizzBuzzer(i)

def buzz: FizzBuzzer => String = fb => fb match {
  case FizzBuzzer(i, s) if (i % 5 == 0) => s + "Buzz"
  case FizzBuzzer(i, s) => i.toString()
}

for (i <- 1 to 100) yield (fizz andThen buzz) (i)

