package com.joshgm3z.downloader.utils

import android.text.format.DateUtils
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import java.text.SimpleDateFormat
import java.util.Date

fun getPrettyTime(instance: Long): String {
    var dateFormat = SimpleDateFormat("h.mm a MMM dd")
    if (DateUtils.isToday(instance)) {
        dateFormat = SimpleDateFormat("h.mm a")
    }
    val date = Date(instance)
    return dateFormat.format(date)
}

fun Long.relativeTime(): String {
    val period = Period(this, System.currentTimeMillis())
    val formatter = PeriodFormatterBuilder()
        .appendYears().appendSuffix(" years ago\n")
        .appendMonths().appendSuffix(" months ago\n")
        .appendWeeks().appendSuffix(" weeks ago\n")
        .appendDays().appendSuffix(" days ago\n")
        .appendHours().appendSuffix(" hours ago\n")
        .appendMinutes().appendSuffix(" min ago\n")
        .appendSeconds().appendSuffix(" sec ago\n")
        .printZeroNever()
        .toFormatter()
    val elapsed = formatter.print(period)
    return elapsed.substringBefore("\n")
}