package com.example.androidadminver1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.androidadminver1.databinding.ProfileAdminLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class profileAdmin : Fragment() {
    private lateinit var binding: ProfileAdminLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileAdminLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener { showThongBao() }
        binding.tvDoiMatKhau.setOnClickListener {
            findNavController().navigate(R.id.doiMatKhau)
        }

        chuyenMH()
        loadAdminInfo()

        binding.tvTroGiup.setOnClickListener {
            checkQuyenGoi()
        }

        binding.imgAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun chuyenMH() {
        binding.btnHome.setOnClickListener { findNavController().navigate(R.id.homeAdmin) }
        binding.btnAdd.setOnClickListener { findNavController().navigate(R.id.chartAdmin) }
        binding.btnProfile.setOnClickListener { findNavController().navigate(R.id.profileAdmin) }
        binding.tvThongBao.setOnClickListener { findNavController().navigate(R.id.notifyAdmin) }
    }

    private fun showThongBao() {
        AlertDialog.Builder(requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
            .setPositiveButton("Đồng ý") { _, _ -> dangXuat() }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun dangXuat() {
        findNavController().navigate(R.id.loginAdmin)
    }

    private fun loadAdminInfo() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRef = FirebaseDatabase.getInstance().getReference("Admin").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val username = snapshot.child("adminname").getValue(String::class.java)
                val imgUrl = snapshot.child("url_img").getValue(String::class.java)

                binding.tvUserName.text = username ?: "Không có tên"

                if (!imgUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(imgUrl)
                        .into(binding.imgAvatar)
                } else {
                    binding.imgAvatar.setImageResource(R.drawable.ic_profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Lỗi tải thông tin: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Chọn ảnh từ thư viện
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            uploadImageToFirebase(uri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().reference.child("avatars/$userId.jpg")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    // Cập nhật đường dẫn ảnh vào Realtime Database
                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(userId)
                        .child("url_img")
                        .setValue(downloadUrl.toString())
                        .addOnSuccessListener {
                            Glide.with(this)
                                .load(downloadUrl.toString())
                                .placeholder(R.drawable.ic_profile)
                                .into(binding.imgAvatar)
                            Toast.makeText(context, "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Lỗi lưu ảnh vào DB: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Upload ảnh thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private val yeuCauQuyen = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted){
            openGoi()
        }else{
            Toast.makeText(requireContext(), "Yêu cầu quyền thất bại", Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkQuyenGoi(){
        if (
            requireContext().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

        )else{
            yeuCauQuyen.launch(Manifest.permission.CALL_PHONE)
        }
    }
    private fun openGoi(){
        val phone = "0898430927"
        val intent  = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phone")
        }
        try {
            startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}
