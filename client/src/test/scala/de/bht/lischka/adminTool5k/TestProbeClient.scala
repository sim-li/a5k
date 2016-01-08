package de.bht.lischka.adminTool5k

import akka.actor.{Actor, Props, ActorRef, ActorSystem}
import de.bht.lischka.adminTool5k.sharedtests.AbstractTestProbe
import utest._
import scala.concurrent.duration.{FiniteDuration, Duration}

/**
  * TODO: AbstractTestProbe is a class. Can we wrap the actor TestProbeCli in another class
  * and have a "clean" companion object?
  */
object TestProbeClient extends AbstractTestProbe {
  val testSystem = ActorSystem()
  var ref: ActorRef = _


  def apply() = {
    ref = testSystem.actorOf(TestProbeClient.props, s"probe${System.currentTimeMillis}")
    this
  }

  def send(receiver: ActorRef, msg: Any) = {
    println("Send called")
  }

  def expectMsg(timeout: FiniteDuration, msgToExpect: Any): Unit = {
    println("Expect msg called")
    assert(true)
  }

  def props(): Props = Props(new TestProbeClient())

}

class TestProbeClient extends Actor {
  override def receive: Actor.Receive = {
    case _ => println("Probe received any")
  }
}
