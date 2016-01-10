import akka.actor.{ActorSystem, ActorRef}
import akka.testkit.TestProbe
import de.bht.lischka.adminTool5k.sharedtests.AbstractTestProbe
import scala.concurrent.duration.{Duration, FiniteDuration}
import akka.testkit

class TestProbeServer extends AbstractTestProbe {
  implicit val system = ActorSystem()

  val probe = TestProbe()

  override def send(target: ActorRef, msg: Any): Unit = probe.send(target, msg)

  override def ref(): ActorRef = probe.ref

  override def expectMsg(timeout: FiniteDuration, msg: Any): Unit = probe.expectMsg(timeout, msg)

  override def expectNoMsg(timeout: FiniteDuration): Unit = probe.expectNoMsg(timeout)

  override def ignoreMessage(f: PartialFunction[Any, Boolean]) = probe.ignoreMsg(f)

  // Gotta type that bitch!
  //  override def within (max: Duration)(f: ⇒ T|)

}
