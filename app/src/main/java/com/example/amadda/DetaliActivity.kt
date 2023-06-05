package com.example.amadda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityDetaliBinding

class DetaliActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetaliBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetaliBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}