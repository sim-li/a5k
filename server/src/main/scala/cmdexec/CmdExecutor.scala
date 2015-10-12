package cmdexec

import models.{ShCmd, ShRes}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.sys.process._

class CmdExecutor {
  /**
   * Executes a shell cmd
   *
   * @param shCmd
   * @return
   */
  def runShCmd(shCmd: ShCmd): Future[ShRes] = {
    Future {
      ShRes(shCmd.cmd !!)
    }
  }
  // Imagine you'd be able to async await a cmd in front end
}

object CmdExecutor {
  def apply(shCmd: ShCmd) = {
    new CmdExecutor().runShCmd(shCmd)
  }
}
