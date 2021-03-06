package org.cheminot.misc

import java.util.Locale
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime => JDateTime, DateTimeZone}
import org.joda.time.format.{ISODateTimeFormat => JISODateTimeFormat}

object DateTime {

  private val parisZone = DateTimeZone.forID("Europe/Paris")

  private val isoFormatter: DateTimeFormatter =
    defaultSettings(JISODateTimeFormat.dateTime)

  def defaultSettings(f: DateTimeFormatter)=
    f.withZone(parisZone).withLocale(Locale.ENGLISH)

  def format(dateTime: JDateTime): String =
    isoFormatter.print(dateTime)

  def parse(s: String): Option[JDateTime] =
    scala.util.Try(parseOrFail(s)).toOption

  def parseOrFail(s: String): JDateTime =
    isoFormatter.parseDateTime(s)

  def fromSecs(t: Long): JDateTime =
    new JDateTime(t * 1000).withZone(parisZone)

  def forPattern(s: String) =
    defaultSettings(DateTimeFormat.forPattern(s))

  def minutesOfDay(datetime: JDateTime): Long =
    (datetime.hourOfDay.get * 60) + datetime.minuteOfHour.get
}
