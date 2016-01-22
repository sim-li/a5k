package de.bht.lischka.adminTool5k.view


import de.bht.lischka.adminTool5k.ModelX.ShellCommand
import BootstrapCSS._
import org.scalajs.dom.document
import rx._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.rx.all._

object ShellCommandEntryView {
  def apply(shellCommand: Var[ShellCommand]) = new ShellCommandEntryView(shellCommand)
}

class ShellCommandEntryView(val shellCommand: Var[ShellCommand]) {
  import rx._

  // @TODO: Any way to simplify this growing mess?
  val commandResponseText: Rx[String] = Rx { shellCommand().executionInfo match {
    case Some(executionInfo) => executionInfo.response
    case _ => "Error"
  }}

  val issueDate: Rx[String] = Rx { shellCommand().issueInfo.commandIssued.toString() }

  val executionDate: Rx[String] = Rx { shellCommand().executionInfo match {
    case Some(executionInfo) => executionInfo.commandExecuted.toString()
    case _ => "Error"
  }}

  val userName: Rx[String] = Rx { shellCommand().issueInfo.user.name }

  val command: Rx[String] = Rx { shellCommand().command }

  // @TODO: Parse this nicely.
  def formatResponse(responseText: String) = {
    responseText.replace("\n", "<br>").replace(" ", "&nbsp;")
  }

  val shellCommandEntry =
    div(cls:=BootstrapCSS.list_group_item)(
      userAndCommandNameSection,
      commandResponseSection,
      issueAndExecutionInfoSection
   )

  def userAndCommandNameSection() = {
    h4(cls:=BootstrapCSS.list_group_item_heading) (
      span(cls:=BootstrapCSS.badge) (userName),
      p(command)
    )
  }

  def commandResponseSection() = {
    p(cls:=BootstrapCSS.list_group_item_text)(
      code(
        h6(
          commandResponseText
        )
      )
    )
  }

  def issueAndExecutionInfoSection() = {
    h6(cls:=BootstrapCSS.list_group_item_footer_text_right)(
      div(
        b(issueDate)
      ),
      div(
        b(
          executionDate
        )
      )
    )
  }

  def render = shellCommandEntry.render
}
