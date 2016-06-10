package org.cheminot.misc

import java.util.concurrent.Executors
import scala.concurrent.{ Future, ExecutionContext, Await }
import scala.concurrent.duration._
import java.util.concurrent.atomic.AtomicInteger

object FutureUtils {

  private def displayProgress(progress: Int) = {
    val p = (progress * 70) / 100
    val todo = 70 - p
    val total = List.fill(p)("#") ++: List.fill(todo)(" ")
    print(s"[${total.mkString}] ${progress}%\r")
  }

  def par[A, B](aaa: Seq[A], progress: Boolean = false)(f: (A) => B)(implicit ec: ExecutionContext): Seq[B] = {
    val n = 20 //aaa.size / (THREADS_PER_POOL * 2)
    if(progress) { Logger.info(s"[progress] total: ${aaa.size} | grouped: ${n}") }
    val counter = new AtomicInteger(0);
    val result = Await.result(
      groupSequentially(aaa, n) { a =>
        Future(f(a)).andThen {
          case _ =>
            if(progress) {
              val progress = scala.math.round(counter.incrementAndGet.toFloat / aaa.size.toFloat * 100)
              displayProgress(progress.toInt)
            }
        }
      },
      1.hours
    )
    result
  }

  def groupSequentially[A, B](seq: Seq[A], i: Int)(f: A => Future[B])(implicit ec: ExecutionContext): Future[Seq[B]] = {
    val groups: Seq[Seq[A]] = seq.grouped(i).toSeq
    traverseSequentially[Seq[A], Seq[B]](groups) { group =>
      Future.sequence(group.map(f))
    }.map(_.flatten)
  }

  def traverseSequentially[A, B](seq: Seq[A])(f: A => Future[B])(implicit ec: ExecutionContext): Future[Seq[B]] = seq match {
    case h +: t => f(h).flatMap { r =>
      traverseSequentially(t)(f) map (r +: _)
    }
    case _ => Future.successful(Seq.empty)
  }
}
