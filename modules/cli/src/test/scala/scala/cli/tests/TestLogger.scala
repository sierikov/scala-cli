package scala.cli.tests

import coursier.cache.CacheLogger
import coursier.cache.loggers.{FallbackRefreshDisplay, RefreshLogger}

import scala.cli.bloop.bloopgun
import scala.cli.Logger

case class TestLogger(info: Boolean = true, debug: Boolean = false) extends Logger {
  def log(s: => String): Unit =
    if (info)
      System.err.println(s)
  def log(s: => String, debug: => String): Unit =
    if (this.debug)
      System.err.println(debug)
    else if (info)
      System.err.println(s)
  def debug(s: => String): Unit =
    if (debug)
      System.err.println(s)

  def withCoursierLogger[T](f: CacheLogger => T): T = {
    val logger = RefreshLogger.create(new FallbackRefreshDisplay)
    logger.use(f(logger))
  }
  def coursierInterfaceLogger: coursierapi.Logger =
    coursierapi.Logger.progressBars() // meh

  def bloopgunLogger: bloopgun.BloopgunLogger =
    bloopgun.BloopgunLogger.nop
  def bloopBspStderr = None
  def bloopBspStdout = None
}