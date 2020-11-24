package com.example.anonymouschat.AnonymousandroidApp.BlogDirectory

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anonymouschat.AnonymousandroidApp.MainBlogActivity
import com.example.anonymouschat.AnonymousandroidApp.R
import com.example.anonymouschat.AnonymousandroidApp.Utils.DiffTime
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class BlogAdapter(private val context: Context,private val data:ArrayList<BlogObject>) : RecyclerView.Adapter<BlogAdapter.Holder>(){
    private val realTime = FirebaseDatabase.getInstance()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid = mAuth.currentUser!!.uid
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title:TextView = itemView.findViewById(R.id.titleBlog)
        private val star:TextView = itemView.findViewById(R.id.textStar)
        private val description:TextView = itemView.findViewById(R.id.descriptionBlog)
        private val comment:TextView = itemView.findViewById(R.id.textComment)
        private val time:TextView = itemView.findViewById(R.id.textTimeBlog)
        private val btnStar:ImageButton = itemView.findViewById(R.id.buttonStar)
        private val btnComment:ImageButton = itemView.findViewById(R.id.buttonComment)
        private val card:MaterialCardView = itemView.findViewById(R.id.cardBlog)
        @SuppressLint("SetTextI18n")
        fun set(position: Int){
            title.text = data[position].title
            star.text = data[position].star.toString()
            comment.text = data[position].cmCount.toString()
            description.text = data[position].description
            time.text = DiffTime().timeDistance(data[position].date, context)
            btnStar.setOnClickListener {
                val ref = realTime.reference.child("Posts").child(data[position].state).child(data[position].key)
                CoroutineScope(IO).launch {
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var starCount = snapshot.child("star").value.toString().toInt()
                            when (btnStar.background.constantState) {
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_baseline_star_24
                                )!!.constantState -> {
                                    btnStar.setBackgroundResource(R.drawable.ic_baseline_star_outline_24)
                                    star.text = (star.text.toString().toInt() - 1).toString()
                                    ref.child("star").setValue(--starCount)
                                    realTime.reference.child("Users").child(uid).child("star")
                                        .child(data[position].key).removeValue()
                                }
                                ContextCompat.getDrawable(
                                    context,
                                    R.drawable.ic_baseline_star_outline_24
                                )!!.constantState -> {
                                    btnStar.setBackgroundResource(R.drawable.ic_baseline_star_24)
                                    star.text = (star.text.toString().toInt() + 1).toString()
                                    ref.child("star").setValue(++starCount)
                                    realTime.reference.child("Users").child(uid).child("star")
                                        .child(data[position].key).setValue(true)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
                }
            }
            card.setOnClickListener {
                val intent = Intent(context,MainBlogActivity::class.java)
                intent.putExtra("createBy",data[position].createBy)
                intent.putExtra("time",data[position].date)
                intent.putExtra("state",data[position].state)
                intent.putExtra("key",data[position].key)
                intent.putExtra("title",data[position].title)
                intent.putExtra("description",data[position].description)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.set(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}