package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityMainBinding
import com.example.amadda.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.timetableBtn.setOnClickListener{
            val intent = Intent(this, TimeTable::class.java)
            startActivity(intent)
        }
        binding.pushAlarmBtn.setOnClickListener{
            val intent = Intent(this, Profile_push::class.java)
            startActivity(intent)
        }
    }
}