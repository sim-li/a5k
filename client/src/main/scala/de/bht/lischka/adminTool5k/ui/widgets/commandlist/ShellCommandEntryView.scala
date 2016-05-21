package de.bht.lischka.adminTool5k.ui.widgets.commandlist

import de.bht.lischka.adminTool5k.ModelX.{WSMessage, ShellCommand}
import de.bht.lischka.adminTool5k.ui.BootstrapCSS
import scalatags.JsDom
import scalatags.JsDom.all._

object ShellCommandEntryView {
  def apply(shellCommand: ShellCommand) = new ShellCommandEntryView(shellCommand)
}

class ShellCommandEntryView(shellCommand: ShellCommand) {
  val STANDARD_WAITING_TEXT = "Executing..."

  val divContainerWithContents = div(id:= shellCommand.issueInfo.id.toString, cls:=BootstrapCSS.list_group_item) (
      contents
   )

  def contents() = div(
    userAndCommandNameSection,
    commandResponseSection,
    issueAndExecutionInfoSection
  )

  def userAndCommandNameSection() = {
    h4(cls:=BootstrapCSS.list_group_item_heading) (
      shellCommand.issueInfo.user.name,
      p(span(cls:="kbd")(shellCommand.command))
    )
  }

  def commandResponseSection() = {
    p(cls:=BootstrapCSS.list_group_item_text) (
      //Bootstrap Code Section
      pre(
          shellCommand.executionInfo.map(e => formatResponse(e.response))
      )
    )
  }

  def formatResponse(responseText: String) = {
    (table /: responseText.split("\n"))((buffer, line: String) =>
      buffer(tr(line))
    )
  }

  def issueAndExecutionInfoSection() = {
    h6(cls:=BootstrapCSS.list_group_item_footer_text_right) (
      div(
        "Issued at ", b(shellCommand.issueInfo.issueTime.toString)
      ),
      div(
        "Executed at ", b(
          shellCommand.executionInfo.map(e => e.executedTime.toString)
        )
      )
    )
  }

  def renderContainer = divContainerWithContents.render

  def renderContents = contents.render
}
