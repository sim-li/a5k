package de.bht.lischka.adminTool5k

import java.util.{UUID, Date}
import de.bht.lischka.adminTool5k.ModelX.Stat
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

  trait Stat extends DataModel

  case class SystemStatsUpdate(stats: SystemStatsLine) extends WSMessage

  case class SystemStatsLine(pid: Pid,
                             processName: Option[ProcessName] = None,
                             cpu: Option[Cpu] = None,
                             time: Option[TimeAlive] = None,
                             memoryUsage: Option[MemoryUsage] = None) extends Stat

  case class Pid(pid: Int) extends Stat

  case class ProcessName(name: String) extends Stat

  case class Cpu(usage: Double) extends Stat

  case class TimeAlive(duration: Duration) extends Stat

  case class MemoryUsage(usage: Long) extends Stat

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
      concreteType[SystemStatsUpdate]

    implicit def statPickler: PicklerPair[Stat] = CompositePickler[Stat].
      concreteType[Stat].
      concreteType[SystemStatsLine].
      concreteType[Pid].
      concreteType[Cpu].
      concreteType[TimeAlive].
      concreteType[MemoryUsage]
  }

}
