package com.stratio.model.spark

import scala.language.implicitConversions
import com.stratio.model.Flight
import org.apache.spark.rdd.RDD

class FlightCsvReader(self: RDD[String]) {

    /**
     *
     * Parser the csv file with the format described in te readme.md file to a Fligth class
     *
     */
    def toFlight: RDD[Flight] = {
      self.map(stringFlight => Flight(stringFlight.split(",")))
    }

    /**
     *
     * Obtain the parser errors
     *
     */
    def toErrors: RDD[(String, String)] = {
      self.map(data => data.split(","))
        .flatMap(row => if (!row(0).forall(_.isDigit))
                          Seq(("ClassCastException", row(0)))
                        else if (!row(15).forall(_.isDigit))
                          Seq(("ClassCastException", row(15)))
                        else if (List(1,2,3,4,5,6,7,8,9,10,11,12).contains(row(1)))
                          Seq(("Missmatch", row(1)))
                        else Seq())
    }
  }

  class FlightFunctions(self: RDD[Flight]) {

    /**
     *
     * Obtain the minimum fuel's consumption using a external RDD with the fuel price by Month
     *
     */
    def minFuelConsumptionByMonthAndAirport(fuelPrice: RDD[String]): RDD[(String, Short)] = ???

    /**
     *
     * Obtain the average distance fliyed by airport, taking the origin field as the airport to group
     *
     */
    def averageDistanceByAirport: RDD[(String, Float)] = ???

    /**
     *
     * Reasign the dest Airport and destHour to the ghost flights being a ghost flight those whom doesn't
     *
     */
    def asignGhostFlights(elapsedSeconds: Int): RDD[Flight] = ???
  }


trait FlightDsl {

  implicit def flightParser(lines: RDD[String]): FlightCsvReader = new FlightCsvReader(lines)

  implicit def flightFunctions(flights: RDD[Flight]): FlightFunctions = new FlightFunctions(flights)
}

object FlightDsl extends FlightDsl

