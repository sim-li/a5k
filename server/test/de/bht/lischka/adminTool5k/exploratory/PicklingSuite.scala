package de.bht.lischka.adminTool5k.exploratory

import de.bht.lischka.adminTool5k.NodeExpr
import prickle.{Pickle, Unpickle}
import utest._
import scala.util.{Failure, Success}
import de.bht.lischka.adminTool5k.exploratory.Model._

object PicklingSuite extends utest.TestSuite {
  import Model.Picklers._

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
        Node.withParameters(
          PPath("my-node-path-123"),
          PHddUsage(3),
          PBandWidth(2.0)
        )
      }
    }

    // Current issue: "Composite Pickler Composition",
//  // Model.Picklers and Actions.Picklers
//    "event orientated object composition" - {
//      assertPicklingAndUnpickling {
//        UpdateModel {
//          Node.withParameters(
//            PPath("my-node-path-123"),
//            PHddUsage(3),
//            PBandWidth(2.0)
//          )
//        }
//      }
//    }

  }
}
