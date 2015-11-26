package de.bht.lischka.adminTool5k.exploratory

import prickle.{CompositePickler, PicklerPair}

object Model {

   case class Node(pars: Seq[NodeExpr]) extends NodeExpr

   object Node {
     def withParameters(els: NodeExpr*): Node = {
       new Node(els.toSeq)
     }
   }

   case class PPath(pathName: String) extends NodeExpr

   case class PHddUsage(usage: Int) extends NodeExpr

   case class PBandWidth(bw: Double) extends NodeExpr

   object Picklers {
     def pickleBuildPlan: PicklerPair[NodeExpr] = CompositePickler[NodeExpr].
       concreteType[Node].
       concreteType[PPath].
       concreteType[PHddUsage].
       concreteType[PBandWidth].

     implicit def pickleTestModelPickler: PicklerPair[NodeExpr] = pickleBuildPlan
   }

 }
