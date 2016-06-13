package org.cheminot.misc.mailer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import rapture.core._
import rapture.http._
import rapture.dom._
import org.cheminot.misc.scheduler.Scheduler

object Mailer {

  sealed trait Msg
  case class AddUp(mail: Mail) extends Msg
  case class Squash(config: MailgunConfig, onError: PartialFunction[Throwable, Unit]) extends Msg

  def init(config: Config, onError: PartialFunction[Throwable, Unit]) =
    Scheduler.schedule(config.mailer.delay, config.mailer.period) {
      case _ => actor.cue(Squash(config.mailgun, onError))
    }

  private val actor = {
    Actor.of[Msg](Seq.empty[Mail]) {
      case Transition(Squash(config, onError), mails) if(!mails.isEmpty) =>
        mails.groupBy(_.subject) map {
          case (_, grouped) if !grouped.isEmpty =>
            val mail = grouped.head
            val count = if(grouped.size > 1) s"[${grouped.size} times] " else ""
            val squashed = mail.copy(subject=count + mail.subject)
            scala.util.Try(Mailgun.send(squashed)(config)).recover(onError)
          case _ =>
        }
        Update(Unit, Seq.empty)

      case Transition(AddUp(mail), mails) =>
        Update(Unit, mail +: mails)

      case Transition(_, mails) =>
        Update(Unit, mails)
    }
  }

  def send(subject: String, message: String)(implicit config: MailgunConfig): Unit = {
    actor.cue(AddUp(Mail(subject, message)))
  }

  def sendException(e: Throwable, dom: Option[DomNode[_, _, _]] = None)(implicit request: HttpRequest, config: MailgunConfig) =
    send(s"[cheminot.web] [${request.requestMethod.toString} ${request.basePathString}] ${e.getMessage}", (dom getOrElse pages.Exception(e)).toString)
}
