package de.bht.lischka.adminTool5k

import java.util.{UUID, Date}
import de.bht.lischka.adminTool5k.ModelX.ProcessInfo
import prickle.{CompositePickler, PicklerPair}

import scala.concurrent.duration.Duration

object ModelX {

  trait WSMessage

  trait DataModel extends WSMessage

  object User {
    def none = User("")
  }

  case class TestWSMessage(description: String) extends WSMessage

  case class User(name: String) extends DataModel

  case class LoginUser(user: User) extends WSMessage

  object ShellCommand {
    def apply(user: User, command: String): ShellCommand = ShellCommand(command, IssueInfo(user))
  }

  case class ShellCommand(command: String, issueInfo: IssueInfo, executionInfo: Option[ExecutionInfo] = None) extends DataModel

  case class ExecuteCommand(shellCommand: ShellCommand) extends WSMessage

  case class CommandResult(shellCommand: ShellCommand) extends WSMessage

  case class ExecutionInfo(response: String, commandExecuted: Date, success: Boolean) extends DataModel

  case class IssueInfo(user: User, id: UUID = UUID.randomUUID(), commandIssued: Date = new Date()) extends DataModel

  trait ProcessInfo extends DataModel

  case class ProcessUpdate(process: Process) extends WSMessage

  case class Process(pid: Pid, processInfo: ProcessInfoBin) extends DataModel

  case class ProcessInfoBin(infoLeft: ProcessInfo, infoRight: Option[ProcessInfo]) extends ProcessInfo

  case class Pid(pid: Int) extends ProcessInfo

  case class ProcessName(name: String) extends ProcessInfo

  case class Cpu(usage: Double) extends ProcessInfo

  case class TimeAlive(duration: Duration) extends ProcessInfo

  case class MemoryUsage(usage: Long) extends ProcessInfo

  object Picklers {
    implicit def basicPickler: PicklerPair[WSMessage] = CompositePickler[WSMessage].
      concreteType[User].
      concreteType[ShellCommand].
      concreteType[ExecutionInfo].
      concreteType[IssueInfo].
      concreteType[LoginUser].
      concreteType[ExecuteCommand].
      concreteType[CommandResult].
      concreteType[TestWSMessage].
      concreteType[ProcessUpdate]

    implicit def statPickler: PicklerPair[ProcessInfo] = CompositePickler[ProcessInfo].
      concreteType[ProcessInfo].
      concreteType[ProcessInfoBin].
      concreteType[Pid].
      concreteType[Cpu].
      concreteType[TimeAlive].
      concreteType[MemoryUsage]
  }

}
