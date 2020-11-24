package com.example.anonymouschat.AnonymousandroidApp.Model

import androidx.lifecycle.LiveData
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject

class BlogRepository {
    lateinit var dataSource: BlogDataSource
    fun fetchBlogProvince(state:String) : LiveData<ArrayList<BlogObject>>{
        dataSource = BlogDataSource()
        dataSource.loadBlog(state)
        return dataSource.resultResponse
    }


}