package com.scala.air.data.utils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object DUtils {

  def main(args: Array[String]) {
    println(DUtils.getDateChange("2016-04-18", -1))
    println(DUtils.getDateChange("2018-02-28", -180))

    println(DUtils.getDateChange(DUtils.getCurrentDate(), -30))
    println(DUtils.getDayForWeek("2019-10-10"))


  }

  def getDayForWeek(date: String): Int = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var cal = Calendar.getInstance()
    cal.setTime(dateFormat.parse(date))
    if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
      7
    } else {
      cal.get(Calendar.DAY_OF_WEEK) - 1;
    }
  }

  def getMonthOfLastDay(date: String): String = {
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    var cal = Calendar.getInstance()
    cal.setTime(dateFormat.parse(date))

    cal.add(Calendar.MONTH,1)
    cal.add(Calendar.DAY_OF_MONTH,-1)
    cal.get(Calendar.DAY_OF_MONTH)
    val formatter = new SimpleDateFormat("yyyy-MM-dd")

    formatter.format(cal.getTime)
  }


  def getDateChange(strCurrentDate: String, year: Int = 0, month: Int = 0, day: Int = 0): String = {
    val iYear = strCurrentDate.substring(0, 4).toInt
    val iMonth = strCurrentDate.substring(5, 7).toInt
    val iDay = strCurrentDate.substring(8, 10).toInt

    var cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, iYear);
    cal.set(Calendar.MONTH, iMonth - 1);
    cal.set(Calendar.DAY_OF_MONTH, iDay);
    cal.add(Calendar.YEAR, year);
    cal.add(Calendar.MONTH, month);
    cal.add(Calendar.DATE, day);
    val currentDate = cal.getTime();
    val formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.format(currentDate)
  }

  def getDateChange(strCurrentDate: String, iQuantity: Int): String = {
    val iYear = strCurrentDate.substring(0, 4).toInt
    val iMonth = strCurrentDate.substring(5, 7).toInt
    val iDay = strCurrentDate.substring(8, 10).toInt

    var cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, iYear);
    cal.set(Calendar.MONTH, iMonth - 1);
    cal.set(Calendar.DAY_OF_MONTH, iDay);
    cal.add(Calendar.DATE, iQuantity);
    val currentDate = cal.getTime();
    val formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.format(currentDate)
  }

  def getTimeDifference(startDateTime: String, endDateTime: String): scala.collection.mutable.Map[String, Long] = {
    val startTime = DUtils.date2Timestamp(startDateTime)
    val endTime = DUtils.date2Timestamp(endDateTime)
    val diffSecond = endTime - startTime

    var map = scala.collection.mutable.Map[String, Long]()
    map += ("second" -> diffSecond)
    map += ("minute" -> (diffSecond / 60).floor.toLong)
    map += ("hour" -> (diffSecond / 3600).floor.toLong)
    map += ("days" -> (diffSecond / 86400).floor.toLong)
    map
  }

  def getCurrentTime(): String = {
    val currentDate = new Date();
    val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    formatter.format(currentDate);
  }

  def getCurrentDate(): String = {
    val currentDate = new Date();
    val formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.format(currentDate);
  }

  def date2Timestamp(tm: String): Long = {
    val fm = new SimpleDateFormat("yyyy-MM-dd")
    (fm.parse(tm).getTime + "").substring(0, 10).toLong
  }
}
