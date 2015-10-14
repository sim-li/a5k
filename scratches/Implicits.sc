val s = "Hello World".split(" ")

case class Name(first: String, last: String)
case class Person(name: Name, age: Integer)


implicit def stringToName(name: String) = {
  val names = name.split(" ")
  Name(names(0), names(1))
}

Person("Alice Anderson", 31)
Person("Bob Bucherson", 32)






