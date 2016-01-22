package de.bht.lischka.adminTool5k.view


import de.bht.lischka.adminTool5k.ModelX.ShellCommand
import BootstrapCSS._
import org.scalajs.dom.document
import rx._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.rx.all._

object ShellCommandEntry {
  def apply(shellCommand: ShellCommand) = new ShellCommandEntry(shellCommand) // Var here, link it up!
}

class ShellCommandEntry(shellCommand: ShellCommand) {
  import rx._
  val commandResponse = Var("Processing command")
  val commandResponseText = Rx(s"${commandResponse()}")
  val commandStatus = Var("Execution pending...")
  val commandStatusText = Rx(s"${commandStatus()}")

  val shellCommandEntry =
    div(id:=shellCommand.issueInfo.commandId.toString(), cls:=BootstrapCSS.list_group_item)(
      userAndCommandName,
      commandResponseSection,
      issueAndExecutionInfo

   )

  def userAndCommandName() = {
    h4(cls:=BootstrapCSS.list_group_item_heading) (
      span(cls:=BootstrapCSS.badge)(
        shellCommand.issueInfo.user.name
      ),
      p(shellCommand.command)
    )
  }

  def commandResponseSection() = {
    p(cls:=BootstrapCSS.list_group_item_text)(
      code(
        h6(id:=s"${shellCommand.issueInfo.commandId.toString()}-command-response")(
          commandResponseText
        )
      )
    )
  }

  def issueAndExecutionInfo() = {
    h6(cls:=BootstrapCSS.list_group_item_footer_text_right)(
      div(id:=s"${shellCommand.issueInfo.commandId}-command-issued")(
        b(shellCommand.issueInfo.commandIssued.toString())
      ),
      div(id:=s"${shellCommand.issueInfo.commandId.toString()}-command-executed")(
        b(
          commandStatusText
        )
      )
    )
  }

  def render = shellCommandEntry.render
}
