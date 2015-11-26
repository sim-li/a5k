package de.bht.lischka.adminTool5k

import de.bht.lischka.adminTool5k.ModelX.WSMessage

object InternalMessages {
  case class SendMessage(msg: WSMessage)
}
