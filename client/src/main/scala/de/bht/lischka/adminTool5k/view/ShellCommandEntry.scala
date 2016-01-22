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
  def apply(shellCommand: ShellCommand) = new ShellCommandEntry(shellCommand)
}

class ShellCommandEntry(val shellCommand: ShellCommand) {
  import rx._

  val commandResponse = Var("Doin something")
  val commandResponseText = Rx(s"${commandResponse()}")
  val commandStatus = Var("Execution pending...")
  val commandStatusText = Rx(s"${commandStatus()}")

  // TOOD: Parse this nicely.
  def formatResponse(responseText: String) = {
    responseText.replace("\n", "<br>").replace(" ", "&nbsp;")
  }

  val shellCommandEntry =
    div(id:=shellCommand.issueInfo.commandId.toString(), cls:=BootstrapCSS.list_group_item)(
      userAndCommandNameSection,
      commandResponseSection,
      issueAndExecutionInfoSection
   )

  def userAndCommandNameSection() = {
    h4(cls:=BootstrapCSS.list_group_item_heading) (
      span(cls:=BootstrapCSS.badge) (
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

  def issueAndExecutionInfoSection() = {
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
