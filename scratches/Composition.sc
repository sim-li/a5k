def x (a: String) = a + "Hell"
def y (b: String) = b + "ooo"
val andThen1 = x _ andThen y _
andThen1("Hi!")

def x1 = (a: String) => a + "|A|"
def y1 = (b: String) => b + "|B|"

val andThen2 = x1.andThen(y1)
andThen2("Mi Moma")

val compose1 = x1.compose(y1)

andThen2("Hi")
compose1("Hi")

