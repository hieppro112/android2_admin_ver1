package com.example.androidadminver1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.androidadminver1.data.notify
import com.example.androidadminver1.databinding.NotifyAdminLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

        binding.btnAddNotify.setOnClickListener {
            addNotify()

            findNavController().navigate(R.id.homeAdmin)

        }
    }

    private fun addNotify() {
        val title = binding.edtTitleNotify.text.toString()
        val content = binding.edtContentNotify.text.toString()

        if (title.isEmpty()) binding.edtTitleNotify.error = "Vui long nhap tieu de"
        if (content.isEmpty()) binding.edtTitleNotify.error = "Vui long nhap noi dung"

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

}