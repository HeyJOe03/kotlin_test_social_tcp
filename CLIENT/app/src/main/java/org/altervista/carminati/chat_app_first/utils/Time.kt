package org.altervista.carminati.chat_app_first.utils

import java.text.SimpleDateFormat
import java.sql.Timestamp
import java.util.*

object Time {
    fun timeStamp(): String{
        val timestamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(timestamp.time)
        return time.toString()
    }
}