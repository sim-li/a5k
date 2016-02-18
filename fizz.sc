for (i <- 1 to 100) yield
  {
    if(i % 3 == 0 && i % 5 == 0) "Fizzbuzz" else
    if(i % 3 == 0) "Fizz" else
    if (i % 5 == 0) "Buzz" else
    i
  }

