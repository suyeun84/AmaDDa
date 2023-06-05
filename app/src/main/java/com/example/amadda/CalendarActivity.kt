package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBtn();
    }

    fun initBtn(){
        binding.buttonTodo.setOnClickListener {
            TodoFragment().show(
                supportFragmentManager, "SampleDialog"
            )
        }
        binding.buttonAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}