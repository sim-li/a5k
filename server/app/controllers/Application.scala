package controllers
import play.api.Play.current
import akka.actor.{Actor, Props, ActorRef}
import be.doeraene.spickling.PicklerRegistry
import models.WSCommunicationCmds
import models.WSCommunicationCmds.User
import play.api.libs.json._
// import the implicits for the Play! JSON pickle format
import be.doeraene.spickling.playjson._
import play.api.mvc.{RequestHeader, WebSocket, Action, Controller}

object Application extends Controller {

//  val system = ActorSystem("ad5k-serv")
//  val pickleManager = system.actorOf(Props(classOf[PickleManager]), "pickleManager")
//  type PickleType = JsValue
//  implicit protected def pickleBuilder: PBuilder[PickleType] = PlayJsonPBuilder
//  implicit protected def pickleReader: PReader[PickleType] = PlayJsonPReader

  object UserActor {
    def props(out : ActorRef) = Props(new UserActor(out))
  }

  class UserActor(out: ActorRef) extends Actor {
    override def receive: Receive = {
      case json: JsValue =>
        println(s"${json}")

//        val unpickledMsg = PicklerRegistry.unpickle(json)



//      case User(name: String) =>
//        println(s"Got a user!${name}, sending Shia")
//        out !
      case _ =>
        println("Got something, who knows.")
    }
  }

//  object PickleManager {
//    def props = Props(new PickleManager())
//  }
//  class PickleManager extends Actor {
//    global.PicklerSetup
//    override def receive: Actor.Receive = {
//      case msg: JsValue =>
//        val unpickledMsg = PicklerRegistry.unpickle(msg)
//        val debugMe = unpickledMsg
//        sender ! unpickledMsg
//    }
//  }

  def index = Action {

    import PicklerRegistry.register
    register[User]

    val ralf = User("Ralf")
    val pickled = PicklerRegistry.pickle(ralf)
    Ok(views.html.index(WSCommunicationCmds.itWorks))
  }

  def socket = WebSocket.acceptWithActor[JsValue, JsValue] {
    request: RequestHeader =>
      out: ActorRef =>
        UserActor.props(out)
  }
}
