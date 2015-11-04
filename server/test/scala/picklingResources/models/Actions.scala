package scala.picklingResources.models

import prickle.{PicklerPair, CompositePickler}

object Actions {
  case class UpdateModel(mod: NodeExpr) extends Action

  object Picklers {
    implicit def ActionsPickler: PicklerPair[NodeExpr] = CompositePickler[NodeExpr].
      concreteType[UpdateModel]
  }
}
