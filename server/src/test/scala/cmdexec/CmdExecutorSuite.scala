package cmdexec

import java.io.IOException

import models.{ShCmd, ShRes}
import utest._

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object CmdExecutorSuite extends utest.TestSuite {

  def tests = TestSuite {
    "should execute correct cmd successfully" - {
      val f: Future[ShRes] = CmdExecutor(ShCmd("ls -al"))
      async {
        val shOutput = await(f).res
        assert(shOutput.length > 0)
      }
    }

    "should execute incorrect cmd unsuccessfully" - {
      intercept[IOException] {
        val cmdEx = CmdExecutor(ShCmd("bogus command"))
        Await.result(cmdEx, Duration("1 s"))
      }
    }

    "require should fail on too short shell cmd" - {
      intercept[IllegalArgumentException] {
        val tooShort = ShCmd("")
      }
    }

    // TODO: Check if failed require would work good for
    // Akka.
    // http://doc.akka.io/docs/akka-stream-and-http-experimental/1.0-RC4/scala/http/routing-dsl/case-class-extraction.html
    // https://github.com/davegurnell/validation
    // https://www.innoq.com/de/blog/validate-your-domain-in-scala/
//    "shell cmd should fail validation with pipes" - {
//      CmdExecutor(ShCmd("ls -al | grep -i HELL"))
//    }
  }

//  // Should also work with simple run Statement
//  tests.runAsync().map {  results =>
////    assert(results.children.toSeq(0).value.isSuccess)
////    assert(results.toSeq(0).value.isSuccess) // root
//    assert(results.toSeq(1).value.isFailure) // testSuccess
//  }
}