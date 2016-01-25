package controllers

import java.util.Date
import javax.management.modelmbean.RequiredModelMBean

import akka.actor.{Actor, Props, ActorRef}
import controllers.Router.{RememberForReplay, ForwardToAllSessions, TestListReceivers}
import de.bht.lischka.adminTool5k.InternalMessages.{SendMessage, RegisterListener}
import de.bht.lischka.adminTool5k.ModelX._

object Router {
  def props = Props(new Router())
  case object TestListReceivers
  case class ForwardToAllSessions(news: Any, ignoredReceiver: ActorRef)
  case class RememberForReplay(msg: Any)
}

class Router extends Actor {
  var registeredReceivers: List[ActorRef] = List()
  var replay: List[SendMessage] = List()

  def forwardMsgToAllSessions(message: Any, ignoredReceiver: ActorRef) = {
    registeredReceivers.
      filter(_ != ignoredReceiver).
      foreach(_ ! message)
  }

  //REPL
  def printAllReceivers() = {
    println(registeredReceivers.foreach((x: ActorRef)  =>
      println(s"Receiver ${x.path} is registered"))
    )
  }

  override def receive: Actor.Receive = {
    case RegisterListener(newReceiver: ActorRef) =>
      registeredReceivers = newReceiver :: registeredReceivers
      replay.foreach(msg => {
        println(s"Sending back repl msg ${msg} to ${newReceiver}")
        newReceiver ! msg
      })

    case ForwardToAllSessions(msg, ignoredReceiver) => forwardMsgToAllSessions(msg, ignoredReceiver)

    case RememberForReplay(msg: SendMessage) =>
      replay = msg :: replay
      println(s"Remembering ${msg} for replay")

    case wsMessage: WSMessage =>
      wsMessage match {
        case CommandResult(cmdResponse)  =>
          val msg = SendMessage(CommandResult(cmdResponse))
          //@TODO: Remove this way of satisfying function parameters
          self ! ForwardToAllSessions(msg, self)
          self ! RememberForReplay(msg)

        case ExecuteCommand(shellCommand) =>
          val msg = SendMessage(ExecuteCommand(shellCommand))
          self ! ForwardToAllSessions(msg, sender())
          self ! RememberForReplay(msg)
          val commandExecutor = context.actorOf(CommandExecutor.props(resultHandler = self))
          commandExecutor ! ExecuteCommand(shellCommand)

        case anything => println(s"Triggered default case in Router, got ${anything}")
      }
  }
}