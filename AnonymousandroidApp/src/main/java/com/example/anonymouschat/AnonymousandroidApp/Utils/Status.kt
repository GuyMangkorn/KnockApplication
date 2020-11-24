package com.example.anonymouschat.AnonymousandroidApp.Utils


const val Poster_Profile = "https://firebasestorage.googleapis.com/v0/b/anonymoussocial-cfece.appspot.com/o/profileImage%2Fuser_santaclaus.png?alt=media&token=92210168-26fa-4496-a81d-ab7db4b9754e"
object Status {
    fun offline() : HashMap<String,Any>{
        return hashMapOf("status" to 0)
    }
    fun online() : HashMap<String,Any>{
        return hashMapOf("status" to 1)
    }
}

