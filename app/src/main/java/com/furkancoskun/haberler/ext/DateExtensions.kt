package com.furkancoskun.haberler.ext

import java.text.SimpleDateFormat
import java.util.*

enum class DateType(val type: Int) {
    DATE_AND_DAY(1),
    HOUR_MINUTE(2),
    DATE_AND_DAY_SHORT(3),
    ADD_TIMESTAMP(4)
}

fun Date.formatDate(dateType: DateType): String {
    return when(dateType) {
        DateType.DATE_AND_DAY -> {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy - EEEE ", Locale("tr"))
            dateFormat.format(this)
        }

        DateType.HOUR_MINUTE -> {
            val dateFormat = SimpleDateFormat("HH:mm", Locale("tr"))
            dateFormat.format(this)
        }

        DateType.DATE_AND_DAY_SHORT -> {
            val dateFormat = SimpleDateFormat("dd MMM EEE", Locale("tr"))
            dateFormat.format(this)
        }

        DateType.ADD_TIMESTAMP -> {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale("tr"))
            dateFormat.format(this)
        }
    }
}