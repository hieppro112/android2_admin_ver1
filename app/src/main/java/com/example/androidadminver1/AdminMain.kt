package com.example.androidadminver1

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidadminver1.data.notify
import com.example.androidadminver1.databinding.AdminLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminMain : AppCompatActivity() {
    private lateinit var fireBaseRef:DatabaseReference

    private lateinit var binding:AdminLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = AdminLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseRef=FirebaseDatabase.getInstance().getReference("Notify")
//        import_dl()
    }


    fun import_dl(){
        val id_noti = fireBaseRef.push().key!!
        fireBaseRef.child(id_noti).setValue(notify(id_noti,"XeXin xin thong bao","day la nen tang ba la ksajfkla asfas"))
            .addOnCompleteListener { Log.d("hieppro", "them thong bao: ") }
            .addOnFailureListener {  Log.d("hieppro", "them thong bao loi: ")}
    }
}