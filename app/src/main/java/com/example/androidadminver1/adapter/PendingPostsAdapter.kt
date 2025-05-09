package com.example.androidadminver1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidadminver1.data.Post
import com.example.androidadminver1.R

class PendingPostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PendingPostsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.post_title)
        val approveButton: Button = view.findViewById(R.id.approve_button)
        val imageView: ImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_pending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = posts[position].title
        holder.imageView.setImageResource(posts[position].imageResId)
        holder.approveButton.setOnClickListener {
            // Xử lý phê duyệt bài viết
        }
    }

    override fun getItemCount() = posts.size
}