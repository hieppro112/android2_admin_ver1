package com.example.androidadminver1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidadminver1.R
import com.example.androidadminver1.data.post
import com.example.androidadminver1.databinding.ItemPostBinding
import com.example.androidadminver1.databinding.ItemPostPendingBinding
import com.example.androidadminver1.homeAdminDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ApprovedPostsAdapter(private val posts: List<post>) : RecyclerView.Adapter<ApprovedPostsAdapter.ViewHolder>() {
    private lateinit var firebaseRef:DatabaseReference

    class ViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseRef=FirebaseDatabase.getInstance().getReference("Post-main")
        val items = posts[position]
        with(holder.binding) {
            postTitle.text = items.tieude
            if (items.Url.isNotEmpty()) {
                Glide.with(holder.itemView)
                    .load(items.Url)
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(postImage)
            } else if (items.Url2.isNotEmpty()) {
                Glide.with(holder.itemView)
                    .load(items.Url2)
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(postImage)
            } else if (items.Url3.isNotEmpty()) {
                Glide.with(holder.itemView)
                    .load(items.Url3)
                    .centerCrop()
                    .error(R.drawable.error)
                    .into(postImage)
            } else {
                Glide.with(holder.itemView)
                    .load(R.drawable.error)
                    .into(postImage)
            }

            viewButton.setOnClickListener {
                val action = homeAdminDirections.actionHomeAdminToInforMotoAdmin(
                    items.id,
                    items.Url,
                    items.soluong,
                    items.giaban.toString(), // Thử truyền String tĩnh
                    items.ghim,
                    items.tieude,
                    items.mota,
                    items.tinhtrang,
                    items.loaixe,
                    items.namsx,
                    items.sdt,
                    items.nsx,
                    items.Url2.toString(),
                    items.Url3.toString(),
                )
                Navigation.findNavController(holder.itemView).navigate(action)

            }
            deleteButton.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete item ?")
                    .setMessage("Confirm delete")
                    .setPositiveButton("yes"){_,_->
//                        val fireBaseRef = FirebaseDatabase.getInstance().getReference("Contacs")
                        firebaseRef.child(items.id.toString()).removeValue()
                            .addOnCompleteListener { Log.d("delete", "delete succses: ") }
                            .addOnFailureListener { Log.d("delete", "delete Fail: ") }
                    }
                    .setNegativeButton("No"){_,_->
                        Log.d("delete", "Cancel: ")
                    }
                    .show()
                return@setOnClickListener
            }
        }
    }

    override fun getItemCount() = posts.size
}