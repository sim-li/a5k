package scala.pickleTestResources

import prickle.{CompositePickler, PicklerPair}
import scala.pickleTestResources.actions.NodeExpr

object PickleTestModel {

  case class PNode(pars: Seq[NodeExpr]) extends NodeExpr

  object PNode {
    def withParameters(els: NodeExpr*): PNode = {
      new PNode(els.toSeq)
    }
  }

  case class PPath(pathName: String) extends NodeExpr

  case class PHddUsage(usage: Int) extends NodeExpr

  case class PBandWidth(bw: Double) extends NodeExpr

  object Picklers {
    implicit def pickleTestModelPickler: PicklerPair[NodeExpr] = CompositePickler[NodeExpr].
      concreteType[PNode].
      concreteType[PPath].
      concreteType[PHddUsage].
      concreteType[PBandWidth]
  }
}