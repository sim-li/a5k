package de.bht.lischka.adminTool5k.sharedtests

import akka.actor.ActorRef

import scala.concurrent.duration.{FiniteDuration, Duration}

abstract class AbstractTestProbe {

  def send(target: ActorRef, msg: Any)

  def expectMsg(timeout: FiniteDuration, msg: Any)

  def ref(): ActorRef
}
