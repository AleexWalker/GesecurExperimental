package com.gesecur.app.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class DateUtils {
    private val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val serviceFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())

    private val calendar = Calendar.getInstance()

    init {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }
    }

    fun getMonthStartDay(date: Date): Date {
        val monthCalendar = Calendar.getInstance()
        monthCalendar.time = date
        monthCalendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        return monthCalendar.time
    }

    fun getMonthEndDay(date: Date): Date {
        val monthCalendar = Calendar.getInstance()
        monthCalendar.time = date
        monthCalendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            clear(Calendar.MINUTE)
            clear(Calendar.SECOND)
            clear(Calendar.MILLISECOND)
        }

        monthCalendar.set(
                Calendar.DAY_OF_MONTH,
                monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        return monthCalendar.time
    }

    fun getWeekStartDay(): String {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        return df.format(calendar.time)
    }

    fun getWeekEndDay(): String {
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        return df.format(calendar.time)
    }

    fun formatDate(date: Date): String {
        return df.format(date)
    }

    fun formatDate(date: Long): String {
        calendar.timeInMillis = date
        return df.format(calendar.time)
    }

    fun toServiceDate(date: Date): String {
        return serviceFormat.format(date)
    }




    companion object {

        fun generateWeekDates(): List<LocalDate> {
            val calendar = Calendar.getInstance(Locale.forLanguageTag("es"))
            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            calendar.add(Calendar.DAY_OF_MONTH, 7)

            val last = calendar.time

            calendar.add(Calendar.DAY_OF_MONTH, -7*8)

            val weekdays = arrayListOf<LocalDate>()

            while (calendar.time.before(last)) {
                weekdays.add(LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()).toLocalDate())
                calendar.add(Calendar.DAY_OF_WEEK, 1)
            }

            return weekdays
        }

        fun localDateFromLong(date: Long): LocalDate {
            return Instant.ofEpochMilli(date*1000).atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}

fun LocalDateTime.toGesecurFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("dd MMM yyyy - HH:mm"))
}

fun LocalDate.toToolbarFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale.forLanguageTag("es")))
}

fun LocalDate.toServerFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun LocalDate.formatRender(): String {
    return this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}

fun LocalDateTime.toServerFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

fun LocalDateTime.toHour(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}