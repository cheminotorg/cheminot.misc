package org.cheminot.misc

object Debug {

  def measure[A](label: String)(f: => A)(g: Double => Unit): A = {
    val start = System.currentTimeMillis
    val x = f
    g(System.currentTimeMillis - start)
    x
  }
}
