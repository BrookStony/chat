package com.seecent.util

import org.joda.time.DateTime

import java.text.SimpleDateFormat

class DateUtil {
    private static final SimpleDateFormat DF = new SimpleDateFormat("EEE yyyy-MM-dd HH:mm")
    private static final SimpleDateFormat DF2 = new SimpleDateFormat("HH:mm")
    private static final SimpleDateFormat DF3 = new SimpleDateFormat("yyyy-MM-dd")

    static String format(Date date){
        return  DF.format(date)
    }

    static String formatLimitHour(Date date){
        return  DF2.format(date)
    }

    static String formatYear(Date date){
        return DF3.format(date)
    }

    static lastDateBetween(Date endDate, Integer dayAgo) {
        def dateTime = new DateTime(endDate).withTime(0, 0, 0, 0)
        def beginDate = dateTime.minusDays(dayAgo).toDate()
        [endDate, beginDate]
    }

    static dateBetween(String type) {
        if("today".equals(type)){
            return todayBetween()
        }
        else if("yesterday".equals(type)) {
            return yesterdayBetween()
        }
        else if("beforeYesterday".equals(type)) {
            return beforeYesterdayBetween()
        }
        else {
            return fivedaysBetween()
        }
    }

    static todayBetween() {
        def endDate = new DateTime().withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static yesterdayBetween() {
        def date = new DateTime().minusDays(1)
        def endDate = date.withTime(23, 59, 59, 0).toDate()
        def beginDate = date.withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static beforeYesterdayBetween() {
        def date = new DateTime().minusDays(2)
        def endDate = date.withTime(23, 59, 59, 0).toDate()
        def beginDate = date.withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static towdaysBetween() {
        def endDate = new DateTime().withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(1).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static threedaysBetween() {
        def endDate = new DateTime().withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(2).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static fivedaysBetween() {
        def endDate = new DateTime().withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(4).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static beforeTowdaysBetween() {
        def endDate = new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(2).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static beforeThreedaysBetween() {
        def endDate = new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(3).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static beforeFivedaysBetween() {
        def endDate = new DateTime().minusDays(1).withTime(23, 59, 59, 0).toDate()
        def beginDate = new DateTime().minusDays(5).withTime(0, 0, 0, 0).toDate()
        [endDate, beginDate]
    }

    static firstDayOfMonth() {
        def dateTime = new DateTime()
        def days = dateTime.dayOfMonth - 1
        return dateTime.minusDays(days).withTime(0, 0, 0, 0).toDate()
    }
}
