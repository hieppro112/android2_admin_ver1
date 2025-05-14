package com.example.androidadminver1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidadminver1.databinding.InforMotoAdminLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class inforMotoAdmin : Fragment() {
    private lateinit var binding:InforMotoAdminLayoutBinding
    private val agrs:inforMotoAdminArgs by navArgs()
    private lateinit var firebaseRef:DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=InforMotoAdminLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chuyenanh()

        firebaseRef= FirebaseDatabase.getInstance().getReference("Post-main")
        setvalue()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        pheduyet()
    }
    fun chuyenanh(){
        binding.flipperInfoMoto.flipInterval=3000
        binding.flipperInfoMoto.isAutoStart=true
        binding.flipperInfoMoto.startFlipping()

        // thuc hien các hình ảnh đem về và thay đổi
        Log.d("link img", "${agrs.UrlImg} ")
        Log.d("link img", "${agrs.UrlImg2} ")
        Log.d("link img", "${agrs.UrlImg3} ")

        agrs.UrlImg?.let {
            Glide.with(this)
                .load(it)
                .centerCrop()
                .fitCenter()
                .error(R.drawable.error)
                .into(binding.imgMoto1)
        }
        agrs.UrlImg2?.let {
            Glide.with(this)
                .load(it)
                .error(R.drawable.error)
                .into(binding.imgMoto2)
        }
        agrs.UrlImg3?.let {
            Glide.with(this)
                .load(it)
                .error(R.drawable.error)
                .into(binding.imgMoto3)
        }

    }

    private fun setvalue() {
        binding.apply {

//            imgMoto1.setImageResource(R.drawable.error)
//            imgMoto2.setImageResource(R.drawable.imag_moto2)
//            imgMoto3.setImageResource(R.drawable.img_moto3)
            var ghim:Boolean = agrs.ghim
            var id = agrs.idPost
            val sdt = agrs.sdt
            txtNsx.text = agrs.nhasx
            soluongMoto.setText(agrs.postSoluong.toString())
            giabanMoto.setText(agrs.postGiaban)
            titleMoto.setText(agrs.postTieude)
            motaMoto.setText(agrs.postMota)
            tinhtrangMoto.text = when(agrs.postTinhtrang){
                0->"Còn mới"
                1->"Đã qua sử dụng"
                else->"Khong xac dinh"
            }
            loaiMoto.setText(agrs.postLoaixe)
            nxsMoto.setText(agrs.postNsx.toString())
        }
    }

    fun pheduyet(){
        binding.tvTuchoi.setOnClickListener {
            firebaseRef.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snap: DataSnapshot) {
                    if (snap.exists()){
                        for (item in snap.children){
                            item?.let {
                                if (agrs.idPost==it.key){
                                    firebaseRef.child(item.key!!).child("duyet")
                                    Toast.makeText(requireContext(),"bai viet da bi tu choi!!",Toast.LENGTH_LONG).show()
                                    firebaseRef.child(it.key!!).child("duyet").setValue(3)
                                    findNavController().navigate(R.id.homeAdmin)

                                }
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("log loi", "loi: $p0: ")
                }

            })
        }


        binding.tvAccep.setOnClickListener{
            firebaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snap: DataSnapshot) {
                    if (snap.exists()){
                        for (item in snap.children){
                            item?.let {
                                if (agrs.idPost==it.key){
                                    Log.d("tesst duyet", "${firebaseRef.child(it.key!!)} ")
                                    Toast.makeText(requireContext(),"bai viet da duoc duyet se som hien len!!",
                                        Toast.LENGTH_LONG).show()
                                    firebaseRef.child(it.key!!).child("duyet").setValue(2)
                                    findNavController().navigate(R.id.homeAdmin)

                                }
                            }
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("log test", "loi : $p0: ")
                }

            })

        }
    }

}