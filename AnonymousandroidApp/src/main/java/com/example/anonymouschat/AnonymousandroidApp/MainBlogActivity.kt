package com.example.anonymouschat.AnonymousandroidApp

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anonymouschat.AnonymousandroidApp.CommentDirectory.CommentAdapter
import com.example.anonymouschat.AnonymousandroidApp.Model.CommentRepository
import com.example.anonymouschat.AnonymousandroidApp.Utils.DiffTime
import com.example.anonymouschat.AnonymousandroidApp.Utils.Poster_Profile
import com.example.anonymouschat.AnonymousandroidApp.Utils.RandomProfile
import com.example.anonymouschat.AnonymousandroidApp.ViewModel.CommentViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import kotlin.random.Random

class MainBlogActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var textSend:TextInputEditText
    private lateinit var btnSendMessage:Button
    private lateinit var profileAuthor:ImageView
    private lateinit var titleText:TextView
    private lateinit var timeAuthor:TextView
    private lateinit var bodyText:TextView
    private lateinit var viewModel:CommentViewModel
    private val realTime = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid:String = mAuth.currentUser!!.uid
    private val repository:CommentRepository = CommentRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_blog)
        val state = intent.getStringExtra("state")!!
        val time  = intent.getLongExtra("time",0)
        val key = intent.getStringExtra("key")!!
        val createBy = intent.getStringExtra("createBy")
        recyclerView = findViewById(R.id.recyclerComment)
        viewModel = getViewModel("สมุทรปราการ",key)
        viewModel.fetchCommentData.observe(this,{
            adapter = CommentAdapter(this, it,createBy)
            layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        timeAuthor = findViewById(R.id.timeBlogPosted)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CoroutineScope(Dispatchers.Default).launch {
                timeAuthor.text = DiffTime().timeFormat(time)
            }
        }
        val ref = realTime.reference.child("Posts").child(state).child(key).child("comments")
        profileAuthor = findViewById(R.id.imageBlogger)
        Glide.with(this).load(Poster_Profile).thumbnail(0.1f).circleCrop().into(profileAuthor)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        textSend = findViewById(R.id.textSendInput)
        titleText = findViewById(R.id.topicText)
        bodyText = findViewById(R.id.bodyBlog)
        intent.getStringExtra("title").let {
            titleText.text = it
        }
        intent.getStringExtra("description").let {
            bodyText.text = it
        }
        btnSendMessage.setOnClickListener {
            val listProfile = RandomProfile().listProfile()
            val time = Timestamp(System.currentTimeMillis()).time
            Log.d("TAG_SEND", "yes")
            if(textSend.text!!.isNotEmpty()){
                val data:HashMap<String, Any> = hashMapOf(
                    "text" to textSend.text.toString(),
                    "createBy" to uid,
                    "time" to time,
                    "image" to listProfile[Random.nextInt(0, listProfile.size - 1)]
                )
                val refAdd = ref.push()
                refAdd.updateChildren(data)
                val inputManager: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                textSend.text!!.clear()
                textSend.clearFocus()
            }
        }
        textSend.onFocusChangeListener = View.OnFocusChangeListener { _, p1 ->
            if(p1){
                textSend.hint = ""
            }else{
                textSend.hint = getString(R.string.please_write)
            }
        }
        textSend.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    btnSendMessage.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MainBlogActivity,
                            R.color.darkGrey
                        )
                    )
                } else {
                    btnSendMessage.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@MainBlogActivity,
                            R.color.amber_700
                        )
                    )
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }
    private fun getViewModel(state:String,postId:String): CommentViewModel {
        return ViewModelProviders.of(this@MainBlogActivity,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CommentViewModel(repository,state,postId) as T
            }
        }).get(CommentViewModel::class.java)
    }
}