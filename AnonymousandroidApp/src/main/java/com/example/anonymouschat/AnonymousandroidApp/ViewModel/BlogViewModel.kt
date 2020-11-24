package com.example.anonymouschat.AnonymousandroidApp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject
import com.example.anonymouschat.AnonymousandroidApp.Model.BlogRepository

class BlogViewModel(private val repository: BlogRepository,private val state:String) : ViewModel() {
    val fetchAllBlog : LiveData<ArrayList<BlogObject>> by lazy {
        repository.fetchBlogProvince(state)
    }
}