package com.example.androidadminver1

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androidadminver1.databinding.ForgetAdminAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class forgetAdmin : Fragment() {
    private lateinit var binding: ForgetAdminAdminBinding
    private lateinit var firebaseRefAdmin: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = ForgetAdminAdminBinding.inflate(inflater, container, false)
        firebaseRefAdmin = FirebaseDatabase.getInstance().getReference("Admin")
        auth = FirebaseAuth.getInstance()
        binding.btnAcceptAdmin.setOnClickListener {
            val email = binding.etForgotPassAdmin.text.toString().trim()

            checEmail(email)
        }
        return binding.root
    }
    private fun checEmail(email: String) {

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etForgotPassAdmin.error = "Email không hợp lệ"
            return
        }
        firebaseRefAdmin.get().addOnSuccessListener { snapshot ->
            // Duyet qua ds email trong node admin
            for (admin in snapshot.children) {
                val adminEmail = admin.child("email").value.toString()

                // neu email = nhau
                if (adminEmail.equals(email, ignoreCase = true)) {
                    val role = admin.child("role").getValue(Int::class.java)
                    //  admin role = 1
                    if (role == 1) {
                        sendResetEmail(email)
                        return@addOnSuccessListener
                    }
                }
            }
            Toast.makeText(context, "Email không tồn tại trong hệ thống", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Lỗi kết nối cơ sở dữ liệu", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sendResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(context, "Đã gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.loginAdmin)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gửi email thất bại", Toast.LENGTH_SHORT).show()
            }
    }
}