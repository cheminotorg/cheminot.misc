package org.cheminot.misc

object Debug {

  def measure[A](label: String)(f: => A): A = {
    val start = System.currentTimeMillis
    val x = f
    Logger.debug(s"[$label]> ${System.currentTimeMillis - start} ms")
    x
  }
}
