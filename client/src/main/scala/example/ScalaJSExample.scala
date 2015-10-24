package example

import be.doeraene.spickling.PicklerRegistry
import global.PicklerSetup
import models.{WSCommunicationCmds}
import org.scalajs.dom.{MessageEvent, Event}
import WSCommunicationCmds.User
import scala.scalajs.js
import org.scalajs.dom
import akka.actor._

class ClientProxy extends Actor {
  def receive = {
    case User(name) =>
      println(s"Received ${name}")
    case _ => println("Received something else")
  }
}



object ScalaJSExample extends js.JSApp {
  val system = ActorSystem("ad5k-ui")

  def wrapToFuture(e: Event) = {
     println("open")
  }

  def main(): Unit = {
    val url = "ws://localhost:9000/at5k/ws"
    val socket = new dom.WebSocket(url)
    val clientProxy = system.actorOf(Props(classOf[ClientProxy]), "clientProxy")
    import PicklerRegistry.register
    register[User]
    
    socket.onopen = wrapToFuture _

    socket.onopen = { (e: Event) => wrapToFuture(e) }

    socket.onopen = { (e: Event) =>
//      val mamba = PicklerRegistry.pickle(User("Black Mamba"))
//      socket.send(JSON.stringify(mamba))
    }
    socket.onmessage = { (e: MessageEvent) =>
//      val data = js.JSON.parse(e.data.toString).asInstanceOf[js.Any]
//      val msg = PicklerRegistry.unpickle(data)
//      clientProxy ! msg
    }
  }
}
