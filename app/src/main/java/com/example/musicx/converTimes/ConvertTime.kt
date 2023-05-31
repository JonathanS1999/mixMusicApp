package com.example.musicx.converTimes

class ConvertTime {
    fun getMilliSecondMinutHors(millisecond: Long): String {
        var timerString = "";
        val secondString: String;
        val hour = (millisecond / (1000 * 60 * 60)).toInt()
        val minutes = (millisecond % (1000 * 60 * 60) / (1000 * 60)).toInt()
        val second = (millisecond % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        if (hour > 0) {
            timerString = "$hour:"
        }
        secondString = if (second < 10) {
            "0$second"
        } else {
            "" + second
        }
        timerString = "$timerString$minutes:$secondString"
        return timerString
    }
}