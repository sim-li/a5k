package global

import be.doeraene.spickling._
import models.UserValidatable
import models.WSCommunicationCmds.User

object PicklerSetup {
  def setup = {
    import PicklerRegistry.register
    register[User]
    register[UserValidatable]
    register[::[Any]]
  }
}
