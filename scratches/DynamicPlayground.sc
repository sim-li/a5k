import scala.language.dynamics

class Test extends Dynamic {
  def applyDynamic(field: String)(args: Any*) = {
    println(s"Got method call ${field} with args ${args}")
  }
}

val x = new Test()
x.test("hi")
