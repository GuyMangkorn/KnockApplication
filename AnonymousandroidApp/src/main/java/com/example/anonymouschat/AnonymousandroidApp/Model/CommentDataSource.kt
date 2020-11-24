package com.example.anonymouschat.AnonymousandroidApp.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anonymouschat.AnonymousandroidApp.CommentDirectory.CommentObject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CommentDataSource {
    private val realTime = FirebaseDatabase.getInstance()
    private val resultAdd:ArrayList<CommentObject> = ArrayList()
    private val result:MutableLiveData<ArrayList<CommentObject>> = MutableLiveData()
    val response:LiveData<ArrayList<CommentObject>>
        get() = result
    fun loadComment(state:String,postID:String){
        val ref = realTime.reference.child("Posts").child(state).child(postID).child("comments")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                for(i in snapshot.children){
                    val key = i.key!!
                    val createBy = i.child("createBy").value.toString()
                    val text = i.child("text").value.toString()
                    val time = i.child("time").value.toString().toLong()
                    val image = i.child("image").value.toString()
                    val obj = CommentObject(createBy,text,time,image)
                    resultAdd.add(obj)
                    if(resultAdd.size == count){
                        addResponse()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addResponse() {
        Observable.just(resultAdd)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.newThread())
            .subscribe {
                result.postValue(it)
            }
    }
}