package com.test.myevent.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

open class DateUtil {

    companion object {

        fun getTimeText(): String{
            val date = Calendar.getInstance().time
            val hour: String = SimpleDateFormat("HH", Locale.getDefault()).format(date)

            return when {
                Integer.parseInt(hour) in 3..10 -> "Pagi"
                Integer.parseInt(hour) in 10..15 -> "Siang"
                Integer.parseInt(hour) in 15..18 -> "Sore"
                else -> "Malam"
            }
        }

        private val monthLabels = arrayOf(
            "January_Januari",
            "February_Februari",
            "March_Maret",
            "April_April",
            "May_Mei",
            "June_Juni",
            "July_Juli",
            "August_Agustus",
            "September_September",
            "October_Oktober",
            "November_November",
            "December_Desember"
        )

        fun simplifyDateFormat(date1: String?, inputTime: String?, outputTime: String?): String {
            val tz = TimeZone.getTimeZone("GMT+07:00")
            var timeDisplay: String? = null
            val calendar = Calendar.getInstance(tz)
            val inputFormatTime = SimpleDateFormat(inputTime, Locale.US)
            val outputFormatTime = SimpleDateFormat(outputTime, Locale.US)
            outputFormatTime.calendar = calendar
            var date: Date? = null
            var dateString = ""
            if (date1 != null) {
                try {
                    date = inputFormatTime.parse(date1)
                    calendar.time = date
                    timeDisplay = outputFormatTime.format(calendar.time)
                    for (month in monthLabels) {
                        val labels = month.split("_".toRegex()).toTypedArray()
                        dateString = timeDisplay.replace(labels[0].toRegex(), labels[1])
                        if (dateString != timeDisplay) break
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            return dateString
        }
    }


}