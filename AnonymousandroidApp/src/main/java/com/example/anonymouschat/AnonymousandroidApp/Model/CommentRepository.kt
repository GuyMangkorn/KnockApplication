package com.example.anonymouschat.AnonymousandroidApp.Model

import androidx.lifecycle.LiveData
import com.example.anonymouschat.AnonymousandroidApp.CommentDirectory.CommentObject

class CommentRepository {
    lateinit var dataSource: CommentDataSource
    fun fetchComment(state:String,postID:String) : LiveData<ArrayList<CommentObject>>{
        dataSource = CommentDataSource()
        dataSource.loadComment(state, postID)
        return dataSource.response
    }
}