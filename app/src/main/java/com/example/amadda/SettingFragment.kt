package com.example.amadda

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amadda.databinding.FragmentSettingBinding
import com.example.amadda.databinding.FragmentSubscribeBinding

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    var userId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userId = arguments?.getString("userId").toString()
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    fun init(){
        binding.timetableBtn.setOnClickListener{
            val intent = Intent(context, TimeTable::class.java)
            startActivity(intent)
        }
        binding.pushAlarmBtn.setOnClickListener{
            val intent = Intent(context, Profile_push::class.java)
            startActivity(intent)
        }
    }
}
