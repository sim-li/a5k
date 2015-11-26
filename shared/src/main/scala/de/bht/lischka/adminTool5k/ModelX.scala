package de.bht.lischka.adminTool5k

import java.util.Date
import prickle.{CompositePickler, PicklerPair}

object ModelX {

  trait WSMessage

  trait DataModel extends WSMessage

  case class Hello(from: String) extends WSMessage

  case class User(name: String) extends DataModel

  case class ShellCommand(command: String, executionInfo: ExecutionInfo, issueInfo: IssueInfo) extends DataModel

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
      concreteType[Hello]

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
