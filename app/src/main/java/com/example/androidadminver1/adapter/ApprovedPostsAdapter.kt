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



class ApprovedPostsAdapter(private val posts: List<Post>) : RecyclerView.Adapter<ApprovedPostsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.post_title)
        val deleteButton: Button = view.findViewById(R.id.delete_button)
        val viewButton: Button = view.findViewById(R.id.view_button)
        val imageView: ImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = posts[position].title
        holder.imageView.setImageResource(posts[position].imageResId) // Giả sử bạn có tài nguyên hình ảnh
        holder.deleteButton.setOnClickListener {
            // Xử lý xóa bài viết
        }
        holder.viewButton.setOnClickListener {
            // Xử lý xem bài viết
        }
    }

    override fun getItemCount() = posts.size
}