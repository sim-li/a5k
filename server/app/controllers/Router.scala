package controllers

import java.util.Date

import akka.actor.{Actor, Props, ActorRef}
import controllers.Router.{TestSendNewsToAllReceivers, TestListReceivers}
import de.bht.lischka.adminTool5k.InternalMessages.{SendMessage, RegisterListener}
import de.bht.lischka.adminTool5k.ModelX.{ExecutionInfo, LoginUser, ExecuteCommand, WSMessage}

object Router {
  def props = Props(new Router())
  case object TestListReceivers
  case class TestSendNewsToAllReceivers(news: Any)
}

class Router extends Actor {
  var registeredReceivers = List[ActorRef]()

  //REPL
  def forwardMsg(message: Any) = {
    val ignoredReceiver = sender()
    registeredReceivers.filter(receiver => receiver != ignoredReceiver).
      foreach((receiver: ActorRef)  => receiver ! message)
  }

  //REPL
  def printAllReceivers() = {
    println(registeredReceivers.foreach((x: ActorRef)  =>
      println(s"Receiver ${x.path} is registered"))
    )
  }

  override def receive: Actor.Receive = {

    case RegisterListener(session: ActorRef) =>
      registeredReceivers = session :: registeredReceivers

    case TestListReceivers =>
      sender ! registeredReceivers

    case TestSendNewsToAllReceivers(news: Any) =>
      registeredReceivers.map(receiver => receiver ! news)

    case wsMessage: WSMessage =>
      wsMessage match {
        case ExecuteCommand(shellCommand) =>
          context.actorOf(CommandExecutor.props(resultHandler = sender)) ! ExecuteCommand(shellCommand)

        case anything => println(s"Triggered default case in Router, got ${anything}")
      }
  }
}