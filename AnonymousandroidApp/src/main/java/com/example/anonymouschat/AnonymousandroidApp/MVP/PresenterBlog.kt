package com.example.anonymouschat.AnonymousandroidApp.MVP

import android.content.Context
import android.util.Log
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.sql.Timestamp

class PresenterBlog(private val context:InterfaceWrite.ViewWrite) : InterfaceWrite.PresenterWrite {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val realTime = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun postBlog(title: String, description: String) {
        val locationPreferences = (context as Context).getSharedPreferences("Location", Context.MODE_PRIVATE)
        val state:String = locationPreferences.getString("state","Unknown")!!
        val latitude:Double = locationPreferences.getFloat("x", 0F).toDouble()
        val longitude:Double = locationPreferences.getFloat("y", 0F).toDouble()
        val time = Timestamp(System.currentTimeMillis()).time
        val data:HashMap<String,Any> = hashMapOf(
            "title" to title,
            "description" to description,
            "createBy" to mAuth.currentUser!!.uid,
            "time" to time,
            "x" to latitude,
            "y" to longitude,
            "star" to 0,
            "report" to 0
        )
        val ref = realTime.reference.child("Posts").child(state).push()
        realTime.reference.child("Users").child(mAuth.currentUser!!.uid).child("Posts").child(state).child(ref.key!!).setValue(time)
        ref.updateChildren(data)
    }
    private var result:ArrayList<BlogObject> = ArrayList()
    override fun loadBlog(state:String) {
        CoroutineScope(IO).launch {
            Log.d("TAG_GET", state)
            val ref = realTime.reference.child("Posts").child(state)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        val key = snapshot.child(snap.key!!)
                        val title = key.child("title").value.toString()
                        val description = key.child("description").value.toString()
                        val star = key.child("star").value.toString().toInt()
                        val time = key.child("time").value
                        //val obj = BlogObject(state,snap.key!!,title, description, star, time as Long,0)
                        //result.add(obj)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG_GET", error.message)
                }
            })
        }
    }
}