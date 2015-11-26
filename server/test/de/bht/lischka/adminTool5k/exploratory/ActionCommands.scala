package de.bht.lischka.adminTool5k.exploratory

import de.bht.lischka.adminTool5k.{ActionCommand, NodeExpr}

object ActionCommands {
   case class UpdateModel(mod: NodeExpr) extends ActionCommand
 }
