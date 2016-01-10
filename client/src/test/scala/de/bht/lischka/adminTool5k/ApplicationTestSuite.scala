package de.bht.lischka.adminTool5k

import akka.actor.ActorSystem
import de.bht.lischka.adminTool5k.sharedtests.CustomTestRunner
import utest._

object ApplicationTestSuite extends utest.TestSuite with CustomTestRunner {
  override val tests = TestSuite {
    "initialize application actor without throwing exceptions" - {
      val system = ActorSystem("adminTool5k-client")
      system.actorOf(Application.props)
    }
  }
}
