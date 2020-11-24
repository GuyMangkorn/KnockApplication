package com.example.anonymouschat.AnonymousandroidApp.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.anonymouschat.AnonymousandroidApp.R
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class DiffTime {
    @SuppressLint("SimpleDateFormat")
    fun timeDistance(date: Long,context: Context) : String{
        val diff: Long =  Timestamp(System.currentTimeMillis()).time - date
        val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val diffInHours = TimeUnit.MILLISECONDS.toHours(diff)
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diff)
        var timeEnd:String = ""
        when {
            diffInDays.toString().toInt() > 0 -> {
                timeEnd = "$diffInDays ${context.getString(R.string.days)}"
            }
            diffInHours.toString().toInt() > 0 -> {
                timeEnd = "$diffInHours  ${context.getString(R.string.hours)}"
            }
            diffInMinutes.toString().toInt() > 0 -> {
                timeEnd = "$diffInMinutes ${context.getString(R.string.minutes)}"
            }
        }
        return timeEnd
    }
    fun timeFormat(date: Long) : String{
        val sdf = SimpleDateFormat("dd/MM/yy HH:MM")
        val netDate = Date(date)
        val dateTime =sdf.format(netDate)
        return dateTime.toString()
    }
}