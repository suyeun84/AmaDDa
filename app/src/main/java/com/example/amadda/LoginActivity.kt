package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var rdb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBtn();
    }
    fun initBtn(){

        binding.textViewSignUpLink.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener{
//            if(val query = rdb.orderByChild("id").equalTo(binding.editTextId.toString())){}
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

}


