def giveMeATest(): String = {
  "Test"
}
println(giveMeATest())

class TestLibrary {
  def giveMeATest(): String = {
    "Test"
  }
}

val tl = new TestLibrary()

println(tl.giveMeATest())

tl.giveMeATest() == "Test"
