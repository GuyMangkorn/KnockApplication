package com.example.anonymouschat.AnonymousandroidApp.Utils

import com.example.anonymouschat.AnonymousandroidApp.R

class RandomProfile {
    private val result:ArrayList<Int> = ArrayList()
    fun listProfile() : ArrayList<Int>{
        result.add(R.drawable.user_bear)
        result.add(R.drawable.user_boy)
        result.add(R.drawable.user_deerboy)
        result.add(R.drawable.user_deergirl)
        result.add(R.drawable.user_girl)
        result.add(R.drawable.user_grandma)
        result.add(R.drawable.user_penguin)
        result.add(R.drawable.user_reindeer)
        result.add(R.drawable.user_snowman)
        return result
    }
}