package com.example.anonymouschat.AnonymousandroidApp.MVP

import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject

interface InterfaceWrite {
    interface ViewWrite{
        fun resultPost(result:ArrayList<BlogObject>)
    }
    interface PresenterWrite{
        fun postBlog(title:String,description:String)
        fun loadBlog(state:String)
    }
}