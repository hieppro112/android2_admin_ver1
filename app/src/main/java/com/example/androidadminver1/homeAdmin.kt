package com.example.androidadminver1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidadminver1.adapter.ApprovedPostsAdapter
import com.example.androidadminver1.adapter.PendingPostsAdapter
import com.example.androidadminver1.data.post
import com.example.androidadminver1.databinding.HomeAdminLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class homeAdmin : Fragment() {
    private lateinit var binding:HomeAdminLayoutBinding
    private lateinit var listPost:ArrayList<post>
    private lateinit var firebaseRef:DatabaseReference
    private lateinit var adapterPost: PendingPostsAdapter

    private lateinit var listPost_daduyet:ArrayList<post>
    private lateinit var adapterPost_daduyet: ApprovedPostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=HomeAdminLayoutBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chuyenMH()
//        setupRecyclerViews()
        firebaseRef = FirebaseDatabase.getInstance().getReference("Post-main")
        listPost= arrayListOf()
        listPost_daduyet= arrayListOf()

        adapterPost = PendingPostsAdapter(listPost)
        binding.pendingPostsRecyclerView.adapter=adapterPost
        binding.pendingPostsRecyclerView.layoutManager=LinearLayoutManager(context)

        adapterPost_daduyet=ApprovedPostsAdapter(listPost_daduyet)
        binding.approvedPostsRecyclerView.adapter=adapterPost_daduyet
        binding.approvedPostsRecyclerView.layoutManager=LinearLayoutManager(context)

        fetchData_daduyet()
        fetchData()
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                listPost.isEmpty()
                if (snap.exists()){
                    for (item in snap.children){
                        var duyet = item.child("duyet").getValue(Int::class.java)
                        if (duyet==1){
                            item?.let {
                                var post = it.getValue(post::class.java)
                                listPost.add(post!!)
                            }
                        }
                    }
                    binding.pendingPostsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("fech data", "fail: ")
            }

        })
    }

    private fun fetchData_daduyet() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                listPost_daduyet.isEmpty()
                if (snap.exists()){
                    for (item in snap.children){
                        var duyet = item.child("duyet").getValue(Int::class.java)
                        if (duyet==2){
                            item?.let {
                                var post = it.getValue(post::class.java)
                                listPost_daduyet.add(post!!)
                            }
                        }
                    }
                    binding.approvedPostsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("fech data", "fail: ")
            }

        })
    }



    fun chuyenMH(){
        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.homeAdmin)
        }
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.chartAdmin)
        }
        binding.btnProfile.setOnClickListener {
            findNavController().navigate(R.id.profileAdmin)
        }
    }


}