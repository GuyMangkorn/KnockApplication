package com.example.anonymouschat.AnonymousandroidApp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.anonymouschat.AnonymousandroidApp.CommentDirectory.CommentObject
import com.example.anonymouschat.AnonymousandroidApp.Model.CommentRepository

class CommentViewModel(private val repository: CommentRepository,private val state:String,private val postId:String) : ViewModel() {
    val fetchCommentData : LiveData<ArrayList<CommentObject>> by lazy{
        repository.fetchComment(state,postId)
    }
}