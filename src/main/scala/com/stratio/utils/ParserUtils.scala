package com.stratio.utils

import org.joda.time.DateTime

object ParserUtils {

  def getDateTime(year: Int, month: Int, day: Int): DateTime =
    new DateTime()
      .year().setCopy(year)
      .monthOfYear().setCopy(month)
      .dayOfMonth().setCopy(day)
      .minuteOfDay().setCopy(0)
      .secondOfDay().setCopy(0)
      .millisOfDay().setCopy(0)
}
