package com.dhandev.myapp1.utils

import java.util.*

class dateUtil {
    fun getDate(year: String, month: String, day: String): String{
        val birth = Calendar.getInstance()
        val today = Calendar.getInstance()

        birth.set(year.toInt(), month.toInt(), day.toInt())

        var age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)){
            age--
        }
        return age.toString()
    }

    fun getDeathDate(year: String, month: String, day: String, yearDeath: String, monthDeath: String, dayDeath: String): String{
        val birth = Calendar.getInstance()
        val death = Calendar.getInstance()

        birth.set(year.toInt(), month.toInt(), day.toInt())
        death.set(yearDeath.toInt(), monthDeath.toInt(), dayDeath.toInt())
        var age = death.get(Calendar.YEAR) - birth.get(Calendar.YEAR)

        if (death.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)){
            age--
        }
        return age.toString()
    }
}