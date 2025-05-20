package com.example.androidadminver1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.androidadminver1.data.notify
import com.example.androidadminver1.databinding.NotifyAdminLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class notifyAdmin : Fragment() {
    private lateinit var firebaseRef:DatabaseReference

    private lateinit var binding:NotifyAdminLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=NotifyAdminLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseRef=FirebaseDatabase.getInstance().getReference("Notify")
        addNotify()
        chuyenMH()
    }

    private fun addNotify(){
    binding.radioGroP.setOnCheckedChangeListener { group, checkedId ->
        when(checkedId){
            R.id.rd_send_all->{
                binding.sendMember.visibility = View.GONE
                binding.btnAddNotify.setOnClickListener {
                    addNotifyAll()
                    binding.edtTitleNotify.setText("")
                    binding.edtContentNotify.setText("")
                    binding.edtOnlyMember.setText("")
                }
            }
            R.id.rd_send_only->{
                binding.sendMember.visibility = View.VISIBLE
                binding.btnAddNotify.setOnClickListener {
                    xuly_send_only()
                }
            }
        }
    }


    }

    private fun xuly_send_only(){
        val gmail_id = binding.edtOnlyMember.text.toString().trim()
        getIDtoGmail(gmail_id){id_member->
            if (id_member!=null){
                val firebaseSendMember = FirebaseDatabase.getInstance().getReference("notify-member").child("$id_member")
                val id = firebaseSendMember.push().key!!
                val id_post = firebaseSendMember.push().key!!
                val title = binding.edtTitleNotify.text.toString()
                val content = binding.edtContentNotify.text.toString()
                if (title.isEmpty()) {
                    binding.edtTitleNotify.error = "Vui long nhap tieu de"
                    return@getIDtoGmail
                }
                if (content.isEmpty()) {
                    binding.edtContentNotify.error = "Vui long nhap noi dung"
                    return@getIDtoGmail
                }
                if (gmail_id.isEmpty()) {
                    binding.edtOnlyMember.error="Nhap Gmail nguoi nhan"
                    return@getIDtoGmail
                }

                firebaseSendMember.child(id).setValue(notify(id_post,title,content, gmail_id))
                    .addOnCompleteListener { Toast.makeText(requireContext(), "Gui thong bao thanh cong",Toast.LENGTH_SHORT).show() }
                    .addOnFailureListener { Toast.makeText(requireContext(), "Gui thong bao that bai",Toast.LENGTH_SHORT).show() }


            }
            else{
                Toast.makeText(requireContext(),"gmail khong ton tai trong tai khoan nao",Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun getIDtoGmail(gmail:String , callback:(String?)->Unit){
        val firebaseUser = FirebaseDatabase.getInstance().getReference("Users")
        firebaseUser.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snap: DataSnapshot) {
                if (snap.exists()){
                    for (item in snap.children){
                        var checkgmail = item.child("email").getValue(String::class.java)
                        if (gmail== checkgmail){
                            val id = item.child("id").value.toString()
                            callback(id)
                            return
                        }
                    }
                    callback(null)
                    return
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("getidtogmail Fail", "loi:$p0 ")
                callback(null)
            }

        })
    }

    private fun addNotifyAll() {
        val title = binding.edtTitleNotify.text.toString()
        val content = binding.edtContentNotify.text.toString()

        if (title.isEmpty()) {
            binding.edtTitleNotify.error = "Vui long nhap tieu de"
            return
        }
        if (content.isEmpty()){
            binding.edtContentNotify.error = "Vui long nhap noi dung"
            return
        }

        val id_noti = firebaseRef.push().key!!


        firebaseRef.child(id_noti).setValue(notify(id_noti, title, content))
            .addOnCompleteListener {
                Toast.makeText(
                    requireContext(),
                    "Add Notifycation Succses !!",
                    Toast.LENGTH_SHORT
                ).show()

                binding.edtTitleNotify.setText("")
                binding.edtContentNotify.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Add Notifycation FAIL !!", Toast.LENGTH_SHORT)
                    .show()
            }
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