package scala

import utest._
import prickle._
import scala.pickleTestResources._
import scala.pickleTestResources.actions.NodeExpr
import scala.util.{Try, Failure, Success}
import PickleTestModel._

object ServerPickleExperimentsSuite extends utest.TestSuite {
  import PickleTestModel.Picklers._

  def tests = TestSuite {
    def assertPicklingAndUnpickling(sourceData: NodeExpr) = {
      val pickle = Pickle.intoString(sourceData)
      Unpickle[NodeExpr].fromString(pickle) match {
        case Success(result) => assert(result == sourceData)
        case Failure(ex) =>
          println(s"Caught my self an exception when unpickling type ${sourceData.getClass}: ${ex}")
          assert(false)
      }
    }

    "pickle composed registered node hirarchy" - {
      assertPicklingAndUnpickling {
        PNode.withParameters(
          PPath("my-node-path-123"),
          PHddUsage(3),
          PBandWidth(2.0)
        )
      }
    }

  }
}
