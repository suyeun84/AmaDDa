package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.amadda.databinding.ActivitySignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    var flag1 = false
    var flag2 = false
    var flag3 = false

    lateinit var rdb: DatabaseReference
    var findQuery = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initInput()
        initBtn()
    }

    private fun initInput() {
        binding.apply {
            editTextId.addTextChangedListener {
                if (it.toString().contains(Regex("[0-9]")) && it.toString()
                        .contains(Regex("[a-z|A-Z]"))
                ) {
                    flag1 = true
                    binding.IdInputLayout.error = null
                } else {
                    flag1 = false
                    binding.IdInputLayout.error = "올바른 형식을 입력하세요"
                }
                buttonSignin.isEnabled = flag1 && flag2 && flag3

            }
            editTextPassword.addTextChangedListener {
                if (it.toString().length >= 8) {
                    flag2 = true
                    binding.PasswordInputLayout.error = null
                } else {
                    flag2 = false
                    binding.PasswordInputLayout.error = "8자리 이상 입력하세요"
                }
                buttonSignin.isEnabled = flag1 && flag2 && flag3
            }
            editTextPassword2.addTextChangedListener {
                if (it.toString() == binding.editTextPassword.text.toString()) {
                    flag3 = true
                    binding.PasswordInputLayout2.error = null
                } else {
                    flag3 = false
                    binding.PasswordInputLayout2.error = "비밀번호가 일치하지 않습니다."
                }
                buttonSignin.isEnabled = flag1 && flag2 && flag3
            }
        }
    }

    fun initBtn() {
        binding.backBtnAppbar.setOnClickListener {
            finish();
        }
        binding.buttonSignin.setOnClickListener {

            val inputId = binding.editTextId.text.toString()
            val inputPwd = binding.editTextPassword.text.toString()
            val inputPwd2 = binding.editTextPassword2.text.toString()

            //firebase에 회원정보 저장

            rdb = Firebase.database.getReference("Users/user")
            val userinfo = Users(
                binding.editTextId.text.toString(),
                binding.editTextPassword.text.toString(),
                null,
                null,
                null
            )
            rdb.child(inputId).get().addOnSuccessListener {
                if (it.value != null) {
                    flag1 = false
                    binding.IdInputLayout.error = "이미 존재하는 아이디입니다."
                } else if (inputPwd != inputPwd2) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    rdb.child(binding.editTextId.text.toString()).setValue(userinfo)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

//id 중복 안되게 설정
//            val id = binding.editTextId.text.toString()
//            rdb.child(id).child(id).get().addOnSuccessListener {
//                val map = it.value.toString()
//                Log.d("id", map)
//                if(map != null){
//                    Toast.makeText(this, "중복된 아이디가 있습니다.", Toast.LENGTH_SHORT).show()
//                    binding.editTextId.text?.clear()
//                    binding.editTextPassword.text?.clear()
//                    binding.editTextPassword2.text?.clear()
//                }else{
//                    //firebase에 회원정보 저장
//                    rdb = Firebase.database.getReference("Users/user")
//                    val userinfo = Users(binding.editTextId.text.toString(), binding.editTextPassword.text.toString())
//                    rdb.child(binding.editTextId.text.toString()).setValue(userinfo)
//
//                    val intent = Intent(this, LoginActivity::class.java)
//                    startActivity(intent)
//                }
//            }










