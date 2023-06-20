package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.amadda.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var rdb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBtn();
    }

    fun initBtn() {

        binding.textViewSignUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.buttonLogin.setOnClickListener {
            //firebase에 회원정보 저장
            rdb = Firebase.database.getReference("Users/user")
            Log.d("login", "why")
            val inputId = binding.editTextId.text.toString()
            val inputPwd = binding.editTextPassword.text.toString()
            Log.d("login", "id : $inputId, pw : $inputPwd")
            rdb.child(inputId).child("id").get().addOnSuccessListener {
                if (it.value == null) {
                    Toast.makeText(this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 아이디 있음
                    rdb.child(inputId).child("password").get().addOnSuccessListener {
                        if (it.value == inputPwd) {
                            Toast.makeText(this, "환영합니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("userId", inputId)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this, "id, password 데이터 가져오기 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "id, password 데이터 가져오기 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
}