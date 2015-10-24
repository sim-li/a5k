import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}

val name: Future[String] = Future {
  "Alice"
}

name.map(n => n.charAt(0))

name.onComplete{
  name =>
    println(s"My name is ${name}")
}

println("Hello world")


async(name)