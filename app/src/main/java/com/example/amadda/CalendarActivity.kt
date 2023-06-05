package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.amadda.databinding.ActivityCalendarBinding
import java.time.LocalDate
import java.time.Month
import java.time.Year

class CalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    val dateViewModel: DateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDate()
        initFragment()
    }

    fun initDate() {
        val date: LocalDate = LocalDate.now()
        val year: Int = date.year
        val month: Int = date.monthValue - 1
        dateViewModel.setCurYear(year)
        dateViewModel.setCurMonth(month)
    }

    fun initFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = CalendarFragment()
        fragmentTransaction.add(R.id.calendar_fragment_container, fragment)
        fragmentTransaction.commit()
    }
}