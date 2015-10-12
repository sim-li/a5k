package models

object WSCommunicationCmds {
  val itWorks = "Hello Test"
  case class Message(m: Any)
  abstract class AToolMsg()
  abstract class ATReceiver()
  abstract class Model() extends AToolMsg
  abstract class Event() extends AToolMsg
  case class User(name: String)
  case class ConnectUser(user: User) extends Event
  case class DisconnectUser(user: User) extends Event
  case class SignedIn(user:User) extends Event
  case class ReveiveMsg(msg: AToolMsg) extends Event
  case class SendMsg(msg: AToolMsg) extends Event
}
