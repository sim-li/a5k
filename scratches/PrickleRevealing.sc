// Prickle uses copy, a command of case classes.
// Copy returns a copy of the current class.
case class TwoNumbers(x: Int, y:Int) {
  // It offers the possiblity to overwrite all vars
  def makeClone() = {
    copy(x = 100, y = 200)
  }
  // Or just one
  def makeCloneJustOverwriteOneVar() = {
    copy(x = 100)
  }
}
TwoNumbers(1,2)
TwoNumbers(1,2).makeClone()
TwoNumbers(1,2).makeCloneJustOverwriteOneVar()

