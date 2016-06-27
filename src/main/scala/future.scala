package org.cheminot.misc

import java.util.concurrent.Executors
import scala.concurrent.{ Future => JFuture, ExecutionContext, Await }
import scala.concurrent.duration._
import java.util.concurrent.atomic.AtomicInteger

object Future {

  val AVAILABLE_CPUS = Runtime.getRuntime().availableProcessors()

  private def displayProgress(counter: Int, total: Int) = {
    val progress = scala.math.round(counter.toFloat / total.toFloat * 100)
    val p = (progress * 70) / 100
    val todo = 70 - p
    val bar = List.fill(p)("#") ++: List.fill(todo)(" ")
     print(s"[${bar.mkString}] ${progress}% (${counter} / ${total})\r")
  }

  def par[A, B](seq: Seq[A], progress: Boolean = false)(f: (A) => B)(implicit ec: ExecutionContext): Seq[B] = {
    val counter = new AtomicInteger(0)
    val n = seq.length / AVAILABLE_CPUS
    val result = Await.result(
      groupSequentially(seq, n) { a =>
        JFuture(f(a)).andThen {
          case _ if progress =>
            displayProgress(counter.incrementAndGet, seq.size)
        }
      },
      1.hours
    )
    println("")
    result
  }

  def groupSequentially[A, B](seq: Seq[A], i: Int)(f: A => JFuture[B])(implicit ec: ExecutionContext): JFuture[Seq[B]] = {
    val groups: Seq[Seq[A]] = seq.grouped(i).toSeq
    traverseSequentially[Seq[A], Seq[B]](groups) { group =>
      JFuture.sequence(group.map(f))
    }.map(_.flatten)
  }

  def traverseSequentially[A, B](seq: Seq[A])(f: A => JFuture[B])(implicit ec: ExecutionContext): JFuture[Seq[B]] = seq match {
    case h +: t => f(h).flatMap { r =>
      traverseSequentially(t)(f) map (r +: _)
    }
    case _ => JFuture.successful(Seq.empty)
  }
}
