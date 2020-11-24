package com.example.anonymouschat.AnonymousandroidApp.BlogDirectory

import java.io.FileDescriptor

data class BlogObject(
                    val state:String,
                    val key:String,
                    val title:String,
                    val createBy:String,
                    val description:String,
                    val star:Int,
                    val date:Long,
                    val cmCount:Int)
