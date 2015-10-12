package controllers

import akka.actor.{ActorSystem, Actor, ActorRef, Props}
import akka.actor.Actor.Receive
import models.WSCommunicationCmds
import play.api.libs.json.JsValue
import play.api.mvc._
import play.api.Play.current
import be.doeraene.spickling._
import be.doeraene.spickling.PicklerRegistry
import WSCommunicationCmds.User
import be.doeraene.spickling.playjson._

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
        import PicklerRegistry.register
        register[User]
        PicklerRegistry.pickle(User("Ralf"))
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
    Ok(views.html.index(WSCommunicationCmds.itWorks))
  }

  def socket = WebSocket.acceptWithActor[JsValue, JsValue] {
    request: RequestHeader =>
      out: ActorRef =>
        UserActor.props(out)
  }
}
