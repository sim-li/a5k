object Scribble extends App {

  import scala.sys.process._

  val a = "ls -al" !!
  val b = "HELLO"

  a.replace("\n", "<br>")
  a.replace(" ", "&nbsp;")
}