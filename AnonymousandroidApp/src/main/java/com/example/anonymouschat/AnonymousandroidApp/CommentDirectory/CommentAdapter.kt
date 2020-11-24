package com.example.anonymouschat.AnonymousandroidApp.CommentDirectory

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.anonymouschat.AnonymousandroidApp.R
import com.example.anonymouschat.AnonymousandroidApp.Utils.RandomProfile
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.ArrayList
import kotlin.random.Random

class CommentAdapter(
    private val context: Context,
    private val arr: ArrayList<CommentObject>,
    private val createBy: String?
) : RecyclerView.Adapter<CommentAdapter.Holder>() {
    inner class Holder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        private val text: TextView = itemView.findViewById(R.id.textListComment)
        private val image: ImageView = itemView.findViewById(R.id.imagePreview)
        private val card: CardView = itemView.findViewById(R.id.cardComment)
        private val photo: ArrayList<Int> = RandomProfile().listProfile()

        @SuppressLint("CheckResult")
        fun set(position: Int) {
            text.text = arr[position].text
            if (createBy == arr[position].crateBy) {
                Glide.with(context).load(R.drawable.user_santaclaus).thumbnail(0.1f)
                    .circleCrop().into(image)
                image.background = ContextCompat.getDrawable(context,R.drawable.cornor_oval)
            } else {
                Glide.with(context).load(photo[Random.nextInt(0, photo.size - 1)]).thumbnail(0.1f)
                    .circleCrop().into(image)
            }
            card.setOnLongClickListener {
                card.setCardBackgroundColor(context.getColor(R.color.shrine_1))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.set(position)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

}