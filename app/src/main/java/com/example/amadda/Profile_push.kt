package com.example.amadda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityProfileBinding
import com.example.amadda.databinding.ActivityProfilePushBinding
import com.example.amadda.databinding.ActivityTimeTableBinding

class Profile_push : AppCompatActivity() {
    lateinit var binding: ActivityProfilePushBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePushBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
    }

}
