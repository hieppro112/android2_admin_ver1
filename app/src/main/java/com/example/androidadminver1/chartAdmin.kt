package com.example.androidadminver1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidadminver1.databinding.ChartAdminLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chartAdmin : Fragment() {
    private lateinit var binding: ChartAdminLayoutBinding
    private lateinit var fireBaseRef: DatabaseReference
    private lateinit var firebaseRefAdmin: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChartAdminLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Khởi tạo Firebase references
        fireBaseRef = FirebaseDatabase.getInstance().getReference()
        firebaseRefAdmin = FirebaseDatabase.getInstance().getReference("Post-main")

        // Thiết lập điều hướng
        chuyenMH()

        // Lấy dữ liệu từ Firebase
        fetchStatistics()
    }

    private fun fetchStatistics() {
        // Đếm số lượng bài đăng từ Port_khanh dựa trên trường id
        firebaseRefAdmin.get().addOnSuccessListener { snapshot ->
            var postCount = 0
            for (postSnapshot in snapshot.children) {
                val id = postSnapshot.child("id").getValue(String::class.java)
                if (!id.isNullOrEmpty()) {
                    postCount++
                }
            }
            binding.tvSLBaiDang.text = postCount.toString()
            Log.d("chartAdmin", "Số lượng bài đăng: $postCount")
        }.addOnFailureListener { error ->
            Log.e("chartAdmin", "Lỗi khi lấy số lượng bài đăng: ${error.message}", error)
            Toast.makeText(context, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
        }

        // Đếm số lượng người dùng từ Users dựa trên trường email
        fireBaseRef.child("Users").get().addOnSuccessListener { snapshot ->
            var userCount = 0
            for (userSnapshot in snapshot.children) {
                val email = userSnapshot.child("email").getValue(String::class.java)
                if (!email.isNullOrEmpty()) {
                    userCount++
                }
            }
            binding.tvSLNguoiDung.text = userCount.toString()
            Log.d("chartAdmin", "Số lượng người dùng: $userCount")
        }.addOnFailureListener { error ->
            Log.e("chartAdmin", "Lỗi khi lấy số lượng người dùng: ${error.message}", error)
            Toast.makeText(context, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chuyenMH() {
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