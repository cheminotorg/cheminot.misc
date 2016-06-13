package org.cheminot.misc

import org.slf4j.{ Logger => Slf4jLogger, LoggerFactory }

trait LoggerLike {

  val logger: Slf4jLogger

  def isTraceEnabled = logger.isTraceEnabled

  def isDebugEnabled = logger.isDebugEnabled

  def isInfoEnabled = logger.isInfoEnabled

  def isWarnEnabled = logger.isWarnEnabled

  def isErrorEnabled = logger.isErrorEnabled

  def trace(message: => String) {
    if (logger.isTraceEnabled) logger.trace(message)
  }

  def trace(message: => String, error: => Throwable) {
    if (logger.isTraceEnabled) logger.trace(message, error)
  }

  def debug(message: => String) {
    if (logger.isDebugEnabled) logger.debug(message)
  }

  def debug(message: => String, error: => Throwable) {
    if (logger.isDebugEnabled) logger.debug(message, error)
  }

  def info(message: => String) {
    if (logger.isInfoEnabled) logger.info(message)
  }

  def info(message: => String, error: => Throwable) {
    if (logger.isInfoEnabled) logger.info(message, error)
  }

  def warn(message: => String) {
    if (logger.isWarnEnabled) logger.warn(message)
  }

  def warn(message: => String, error: => Throwable) {
    if (logger.isWarnEnabled) logger.warn(message, error)
  }

  def error(message: => String) {
    if (logger.isErrorEnabled) logger.error(message)
  }

  def error(message: => String, error: => Throwable) {
    if (logger.isErrorEnabled) logger.error(message, error)
  }
}

class Logger(val logger: Slf4jLogger) extends LoggerLike
