package com.example.anonymouschat.AnonymousandroidApp.MVP


import android.location.LocationManager
import android.util.Log
import com.example.anonymouschat.AnonymousandroidApp.Utils.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PresenterLogin(private val context:InterFaceMain.View) : InterFaceMain.Presenter  {
    private val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val realTime = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mLocationManager: LocationManager
    private lateinit var userId:String
    override fun loginAnonymous() {
        mAuth.signInAnonymously().addOnCompleteListener {
            var ga = 0
            if(it.isSuccessful){
                userId = mAuth.currentUser!!.uid
                val refRealtime =  realTime.reference.child("Users").child(userId)
                val refUser = realTime.reference.child("Users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                       ga =  snapshot.child("star").childrenCount.toInt()
                        Log.d("TAG_STAR",ga.toString())

                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
                refUser.let {
                    Log.d("TAG_STAR",ga.toString())
                }
                Log.d("TAG_LOGIN",userId)
                refRealtime.updateChildren(Status.online())
                context.result("Login Success !!")
                refRealtime.onDisconnect().apply {
                    updateChildren(Status.offline())
                }
            }
        }.addOnFailureListener {
            Log.d("TAG_LOGIN",it.stackTraceToString())
        }
    }




}