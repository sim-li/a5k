package de.bht.lischka.adminTool5k

import de.bht.lischka.adminTool5k.ModelX.{User, WSMessage}

object InternalMessages {

  case class SendMessage(msg: WSMessage)

  case class LoggedIn(user: User)

  case class LoggedOut(user: User)
}
