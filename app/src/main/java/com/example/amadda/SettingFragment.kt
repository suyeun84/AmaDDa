package com.example.amadda

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amadda.databinding.FragmentSettingBinding
import com.example.amadda.databinding.FragmentSubscribeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingFragment : Fragment() {
    lateinit var rdb: DatabaseReference
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
        binding.useEmail.text = userId
        binding.timetableBtn.setOnClickListener{
            val intent = Intent(context, TimeTable::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
        binding.logoutBtn.setOnClickListener{
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.withdrawalBtn.setOnClickListener{
            rdb = Firebase.database.getReference("Users/user/" + userId)
            rdb.removeValue()
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
