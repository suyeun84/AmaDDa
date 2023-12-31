package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
                if (flag1 && flag2 && flag3)
                    buttonSignin.isEnabled = true
            }
            editTextPassword.addTextChangedListener {
                if (it.toString().length >= 8) {
                    flag2 = true
                    binding.PasswordInputLayout.error = null
                } else {
                    flag2 = false
                    binding.PasswordInputLayout.error = "8자리 이상 입력하세요"
                }
                if (flag1 && flag2 && flag3)
                    buttonSignin.isEnabled = true
            }
            editTextPassword2.addTextChangedListener {
                if (it.toString() == binding.editTextPassword.text.toString()) {
                    flag3 = true
                    binding.PasswordInputLayout2.error = null
                } else {
                    flag3 = false
                    binding.PasswordInputLayout2.error = "비밀번호가 일치하지 않습니다."
                }
                if (flag1 && flag2 && flag3)
                    buttonSignin.isEnabled = true
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
            var subArr: ArrayList<Int> = ArrayList<Int>()
            subArr.add(0)
//            var todoArr: ArrayList<Todo> = ArrayList<Todo>()
//            todoArr.add(Todo("20230610", 0, "title", false, false))
            var cateArr: ArrayList<Category> = ArrayList<Category>()
//            cateArr.add(Category(0, "title", "#FFFFFF"))
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

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}












