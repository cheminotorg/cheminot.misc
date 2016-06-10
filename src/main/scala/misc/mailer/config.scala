package org.cheminot.misc.mailer

import scala.concurrent.duration.FiniteDuration
import rapture.net._

trait Config {
  def mailer: MailerConfig
  def mailgun: MailgunConfig
}

case class MailerConfig(
  delay: FiniteDuration,
  period: FiniteDuration
)

case class MailgunConfig(
  from: String,
  to: String,
  endpoint: HttpQuery,
  username: String,
  password: String
)
