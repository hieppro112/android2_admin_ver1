package com.example.androidadminver1

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androidadminver1.databinding.LoginAdminLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class loginAdmin : Fragment() {
    private lateinit var binding: LoginAdminLayoutBinding
    private lateinit var firebaseRefAdmin: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvQuenMatKhau.setOnClickListener {
            findNavController().navigate(R.id.forgetAdmin)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.registerAdminn)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = LoginAdminLayoutBinding.inflate(inflater,container,false)
        firebaseRefAdmin = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            LoginAdmin()
        }
        return binding.root
    }

    private fun LoginAdmin() {
        val email = binding.etLogin.text.toString().trim()
        val pass = binding.etConfirmPass.text.toString().trim()

        val fields = listOf(
            //to: tao mot cap gia tri (Pair) gom 2 phan: (EditText, String).
            binding.etLogin to "Vui lòng nhập Email",
            binding.etConfirmPass to "Vui lòng nhập mật khẩu",
        )
        for ((field, error) in fields) {
            if (field.text.toString().trim().isEmpty()) {
                // Gán lỗi vào dòng đó
                field.error = error
                // Đặt con trỏ vào dòng đó
                field.requestFocus()
                return
            }
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etLogin.error = "Email không hợp lệ"
            binding.etLogin.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Truy vấn Realtime Database theo UID
                        firebaseRefAdmin.child(user.uid).get()
                            .addOnSuccessListener { dataSnapshot ->
                                if (dataSnapshot.exists()) {
                                    val role = dataSnapshot.child("role").getValue(Int::class.java)
                                    if (role == 1) {
                                        Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                        findNavController().navigate(R.id.homeAdmin)
                                    } else {
                                        Toast.makeText(context, "Đây là tài khoản user", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Lỗi khi lấy dữ liệu người dùng: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        // Trường hợp: Email không tồn tại trong hệ thống
                        binding.etLogin.error = "Email không tồn tại. Vui lòng kiểm tra lại."
                        binding.etLogin.requestFocus()

                    } else if (exception is FirebaseAuthInvalidCredentialsException) {
                        // Trường hợp: Mật khẩu sai
                        binding.etConfirmPass.error = "Sai mật khẩu. Vui lòng thử lại."
                        binding.etConfirmPass.requestFocus()

                    } else {
                        // Trường hợp: Lỗi khác
                        Toast.makeText(context, "Đăng nhập thất bại: ${exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }
}
