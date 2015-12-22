package controllers.pickling

import akka.actor.Actor
import de.bht.lischka.adminTool5k.ModelX.WSMessage

object PickleSupport {
  case class ExecuteUnpickle(raw: String)
  case class ExecutePickle(obj: WSMessage)
  case class UnpickleResult(obj: WSMessage)
  case class PickleResult(raw: String)
}
trait PickleSupport {
  self: Actor =>
    import PickleSupport._
    val pickling = context.actorOf(Pickling.props)
    val unpickling = context.actorOf(Unpickling.props)

    def handlePickling: Receive = {
      case ExecutePickle => pickling ! ExecutePickle
      case ExecuteUnpickle => unpickling ! ExecuteUnpickle
    }
}
