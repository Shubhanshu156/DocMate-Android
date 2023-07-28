package com.example.docmate.Utility

class TimeConverter {

    fun convertTo12HourFormat(time24Hour: Int): String? {
        val time = time24Hour.toInt()
        if (time <= 12) {
            return "$time:00 AM"
        }
        return "${time - 12}:00 PM"
    }
}