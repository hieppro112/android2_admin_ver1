package com.example.androidadminver1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidadminver1.adapter.ApprovedPostsAdapter
import com.example.androidadminver1.adapter.PendingPostsAdapter
import com.example.androidadminver1.data.Post
import com.example.androidadminver1.databinding.HomeAdminLayoutBinding


class homeAdmin : Fragment() {
    private lateinit var binding:HomeAdminLayoutBinding

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
        setupRecyclerViews()


    }

    private fun setupRecyclerViews() {
        val approvedPosts = listOf(
            Post("MOTO Z1000 đã qua sử dụng còn mới 90%", R.drawable.img_moto), // Thay bằng tài nguyên thực tế
            Post("Siêu phẩm BMW M 1000 RR 2022", R.drawable.img_moto3)
        )

        val pendingPosts = listOf(
            Post("Xe máy Honda SH 2023", R.drawable.img_moto3),
            Post("Yamaha R15 V4", R.drawable.img_moto)
        )

        // Thiết lập RecyclerView cho bài viết đã duyệt
        binding.approvedPostsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approvedPostsRecyclerView.adapter = ApprovedPostsAdapter(approvedPosts)

        // Thiết lập RecyclerView cho bài viết chưa duyệt
        binding.pendingPostsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.pendingPostsRecyclerView.adapter = PendingPostsAdapter(pendingPosts)
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