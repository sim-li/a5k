package de.bht.lischka.adminTool5k

import java.util.Date
import prickle.{CompositePickler, PicklerPair}

object ModelX {

  trait WSMessage

  trait DataModel extends WSMessage

  object User {
    def none = User("")
  }

  case class TestWSMessage(description: String) extends WSMessage

  case class User(name: String) extends DataModel

  case class LoginUser(user: User) extends WSMessage

  case class ShellCommand(command: String, issueInfo: IssueInfo, executionInfo: Option[ExecutionInfo] = None) extends DataModel

  case class ExecuteCommand(shellCommand: ShellCommand) extends WSMessage

  case class ExecutionInfo(response: String, commandExecuted: Date, success: Boolean) extends DataModel

  case class IssueInfo(user: User, commandIssued: Date) extends DataModel

  trait Stat extends DataModel

  case class SystemStats(stats: Seq[Stat]) extends Stat

  case class Pid(pid: Int) extends Stat

  case class ProcessName(processName: String) extends Stat

  case class Cpu(usage: Double) extends Stat

  case class Time(time: Date) extends Stat

  case class MemoryUsage(usage: Double) extends Stat

  object SystemStats {
    def withParameters(stats: Stat*): SystemStats = {
      new SystemStats(stats.toSeq)
    }
  }

  object Picklers {
    implicit def basicPickler: PicklerPair[WSMessage] = CompositePickler[WSMessage].
      concreteType[User].
      concreteType[ShellCommand].
      concreteType[ExecutionInfo].
      concreteType[IssueInfo].
      concreteType[LoginUser].
      concreteType[ExecuteCommand].
      concreteType[TestWSMessage]

    implicit def statPickler: PicklerPair[Stat] = CompositePickler[Stat].
      concreteType[Stat].
      concreteType[SystemStats].
      concreteType[Pid].
      concreteType[ProcessName].
      concreteType[Cpu].
      concreteType[Time].
      concreteType[MemoryUsage]
  }

}
