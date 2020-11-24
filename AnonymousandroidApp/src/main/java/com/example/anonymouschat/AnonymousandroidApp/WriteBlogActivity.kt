package com.example.anonymouschat.AnonymousandroidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.anonymouschat.AnonymousandroidApp.BlogDirectory.BlogObject
import com.example.anonymouschat.AnonymousandroidApp.MVP.InterfaceWrite
import com.example.anonymouschat.AnonymousandroidApp.MVP.PresenterBlog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WriteBlogActivity : AppCompatActivity() , InterfaceWrite.ViewWrite{
    private lateinit var postButton: FloatingActionButton
    private val presenter:PresenterBlog = PresenterBlog(this)
    private lateinit var editTextDescription:EditText
    private lateinit var editTextTitle:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_blog)
        editTextDescription = findViewById(R.id.descriptionEditText)
        editTextTitle = findViewById(R.id.titleEditText)
        postButton = findViewById(R.id.floatingActionButtonSubmit)
        postButton.setOnClickListener{
            if(editTextDescription.text.isNotEmpty() && editTextTitle.text.isNotEmpty()) {
                presenter.postBlog(editTextTitle.text.toString(),editTextDescription.text.toString())
                finish()
            }else{
            }
        }
    }

    override fun resultPost(result: ArrayList<BlogObject>) {
    }


}