package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.amadda.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInput()
        initBtn()
    }
    private fun initInput(){
        binding.apply {
            editTextId.addTextChangedListener {
                if(it.toString().contains(Regex("[0-9]")) && it.toString().contains(Regex("[a-z|A-Z]"))){
                    binding.IdInputLayout.error=null
                }else
                    binding.IdInputLayout.error="올바른 형식을 입력하세요"
            }
            editTextPassword.addTextChangedListener {
                if(it.toString().length >= 8){
                    binding.PasswordInputLayout.error=null
                }else
                    binding.PasswordInputLayout.error="8자리 이상 입력하세요"
            }
            editTextPassword2.addTextChangedListener {
                if(it.toString() == binding.editTextPassword.text.toString()){
                    binding.PasswordInputLayout2.error=null
                }else
                    binding.PasswordInputLayout2.error="비밀번호가 일치하지 않습니다."
            }
        }
    }
    fun initBtn(){
        binding.backBtnAppbar.setOnClickListener{
            finish();
        }
        binding.buttonSignin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }}