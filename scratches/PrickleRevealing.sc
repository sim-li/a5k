import java.util.Date

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

case class ClassWithMap(demoMap: Map[String, String] = Map.empty) {
  def makeClone(suffix: String) = {
    copy(demoMap = demoMap + ("Hello " + suffix -> "World"))
  }
}

val c = ClassWithMap().makeClone("Hey").makeClone("Jude")

/*
 * Implicit chain
 */
case class Father[A <: AnyRef]()

// @TODO: Unveal Implicit Conversion Chain of CompositePicklers in Prickle,
// CompositePicklers.scala, L. 77.
//
// This here works:
// ================
//   def pickleBuildPlan: PicklerPair[NodeExpr] = CompositePickler[NodeExpr].
//      concreteType[Node].
//      concreteType[PPath].
//      concreteType[PHddUsage].
//      concreteType[PBandWidth].
//      concreteType[UpdateModel]
//  implicit def pickleTestModelPickler: PicklerPair[NodeExpr] = pickleBuildPlan
//
// PicklerPair's concreteType calls pickler and unpicklers concrete type methods.
// These call a copy and add new unpickler plus new classtag to Map.
//
// Could we interfere here and just build a big map or add a merge function?
// Then later activate the implicit mechanism? >> implicit def
//
// Look into Odersky's Implicits, they offer the solution.

