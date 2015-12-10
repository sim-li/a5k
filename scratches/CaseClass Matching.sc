trait RootThing

case class A (some: String, other: B) extends RootThing
case class B (some: String) extends RootThing

var test = A("HUHU", B("HUTOO"))
test match {
  case x: RootThing => println(s"Matched root ${x}")
  case _ => println("Didn't match anything")
}