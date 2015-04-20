package com.stratio.model

import com.stratio.utils.ParserUtils
import org.joda.time.DateTime

sealed case class Cancelled (id: String) {override def toString: String = id}

object OnTime extends Cancelled (id ="OnTime")
object Cancel extends Cancelled (id ="Cancel")
object Unknown extends Cancelled (id ="Unknown")

case class Delays (
    carrier: Cancelled,
    weather: Cancelled,
    nAS: Cancelled,
    security: Cancelled,
    lateAircraft: Cancelled)

case class Flight (date: DateTime, //Tip: Use ParserUtils.getDateTime
    departureTime: Int,
    crsDepatureTime: Int,
    arrTime: Int,
    cRSArrTime: Int,
    uniqueCarrier: String,
    flightNum: Int,
    actualElapsedTime: Int,
    cRSElapsedTime: Int,
    arrDelay: Int,
    depDelay: Int,
    origin: String,
    dest: String,
    distance: Int,
    cancelled: Cancelled,
    cancellationCode: Int,
    delay: Delays)
{
  def isGhost: Boolean = arrTime == -1

  def departureDate: DateTime =
    date.hourOfDay.setCopy(departureTime.toString.substring(0, departureTime.toString.size - 2)).minuteOfHour
      .setCopy(departureTime.toString.substring(departureTime.toString.size - 2)).secondOfMinute.setCopy(0)

  def arriveDate: DateTime =
    date.hourOfDay.setCopy(departureTime.toString.substring(0, departureTime.toString.size - 2)).minuteOfHour
      .setCopy(departureTime.toString.substring(departureTime.toString.size - 2)).secondOfMinute.setCopy(0)
      .plusMinutes(cRSElapsedTime)
}

object Flight{

  /*
  *
  * Create a new Flight Class from a CSV file
  *
  */
  def apply(fields: Array[String]): Flight = {

    val delays = Delays(
      carrier= parseCancelled(fields(24)),
      weather= parseCancelled(fields(25)),
      nAS= parseCancelled(fields(26)),
      security= parseCancelled(fields(27)),
      lateAircraft= parseCancelled(fields(28)))

    Flight(
      date = ParserUtils.getDateTime(fields(0).toInt, fields(1).toInt, fields(2).toInt),
      departureTime= fields(4).toInt,
      crsDepatureTime= fields(5).toInt,
      arrTime= fields(6).toInt,
      cRSArrTime= fields(7).toInt,
      uniqueCarrier= fields(8),
      flightNum= fields(9).toInt,
      actualElapsedTime= fields(11).toInt,
      cRSElapsedTime= fields(12).toInt,
      arrDelay= fields(14).toInt,
      depDelay= fields(15).toInt,
      origin= fields(16),
      dest= fields(17),
      distance= fields(18).toInt,
      cancelled= parseCancelled(fields(21)),
      cancellationCode= fields(22).toInt,
      delay= delays)
  }

  /*
   *
   * Extract the different types of errors in a string list
   *
   */
  def extractErrors(fields: Array[String]): Seq[String] = ???

  /*
  *
  * Parse String to Cancelled Enum:
  *   if field == 1 -> Cancel
  *   if field == 0 -> OnTime
  *   if field <> 0 && field<>1 -> Unknown
  */
  def parseCancelled(field: String): Cancelled = field match {
    case "0" => OnTime
    case "1" => Cancel
    case _ => Unknown
  }
}
