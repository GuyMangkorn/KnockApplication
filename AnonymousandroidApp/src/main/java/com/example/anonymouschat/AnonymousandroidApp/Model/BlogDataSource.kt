package com.example.anonymouschat.AnonymousandroidApp.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.*

class BlogDataSource {
    private val realTime = FirebaseDatabase.getInstance()
    private val result:MutableLiveData<ArrayList<BlogObject>> = MutableLiveData()
    private val resultAdd:ArrayList<BlogObject> = ArrayList()
    val resultResponse:LiveData<ArrayList<BlogObject>>
        get() = result
    fun loadBlog(state:String){
        val ref = realTime.reference.child("Posts").child(state)
        CoroutineScope(IO).launch {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val count = snapshot.childrenCount.toInt()
                    for (data in snapshot.children) {
                        Log.d("TAG_REQUEST", Thread.currentThread().name)
                        val title = data.child("title").value.toString()
                        val description = data.child("description").value.toString()
                        val star = data.child("star").value.toString().toInt()
                        val time = data.child("time").value
                        val createBy = data.child("createBy").value.toString()

                        val cmCount = data.child("comments").childrenCount.toInt()
                        val obj = BlogObject(
                            state,
                            data.key!!,
                            title,
                            createBy,
                            description,
                            star,
                            time as Long,
                            cmCount
                        )
                        resultAdd.add(obj)
                        if (resultAdd.size == count) {
                            Log.d("TAG_COUNT", "${resultAdd.size}  : $count")
                            addData()
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

    }

    private fun addData(){
            Observable.just(resultAdd)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe {
                    result.postValue(it)
                }
    }

}