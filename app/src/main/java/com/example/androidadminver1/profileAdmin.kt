package com.example.androidadminver1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.androidadminver1.databinding.ProfileAdminLayoutBinding


class profileAdmin : Fragment() {
    private lateinit var binding:ProfileAdminLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=ProfileAdminLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chuyenMH()
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