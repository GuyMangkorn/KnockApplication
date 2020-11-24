package com.example.anonymouschat.AnonymousandroidApp.MVP

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

interface InterFaceMain {
    interface View{
        fun result(result:String)
    }
    interface Presenter{
        fun loginAnonymous()
    }
}