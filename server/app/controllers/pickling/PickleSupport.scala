package controllers.pickling

import akka.actor.Actor
import de.bht.lischka.adminTool5k.ModelX.WSMessage

object PickleSupport {
  case class PickleResult(result: Any)
}
trait PickleSupport {
  self: Actor =>
    import PickleSupport._
    val pickling = context.actorOf(Pickling.props)
    val unpickling = context.actorOf(Unpickling.props)
    println("Support started")

    def handlePickling: Receive = {
      case rawMessage: String =>
        println(s"Handling pickling-> Sending ${rawMessage} to pickling")
        pickling ! rawMessage
      case wsMessage: WSMessage => unpickling ! wsMessage
      case pickleResult: PickleResult => context.self ! pickleResult
    }
}
