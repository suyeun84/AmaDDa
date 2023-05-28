package com.example.amadda

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBtn();
    }
    fun initBtn(){
        binding.textViewSignUpLink.setOnClickListener{
            val intent = Intent(this, SelectColorActivity::class.java)
            startActivity(intent)
        }

    }

}


