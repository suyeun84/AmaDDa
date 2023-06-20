package com.example.amadda

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.example.amadda.databinding.ActivityTodoDetailBinding

class TodoDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityTodoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initBtn()
    }

    fun init() {
        @Suppress("DEPRECATION")
        val eventData = intent.getSerializableExtra("data") as EventData
        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val day = intent.getStringExtra("day")

        val date = month + "월 " + day + "일"

        binding.textViewYear.text = year
        binding.textViewDate.text = date

        if (eventData.category == "KBO리그") {
            KBODetail(eventData, date)
        } else if (eventData.category == "건국대 학사일정") {
            konkukDetail(eventData, date)
        } else if (eventData.category == "프리미어리그") {
            PLDetail(eventData, date)
        }


    }
    fun PLDetail(eventData: EventData, date: String) {
        val leagueData = eventData.event.split("/")
        binding.todoDetailTime.visibility = View.VISIBLE
        binding.textViewTime.text = date + " "+ leagueData[1]
        binding.textViewTitle.text = "⚽️ " + leagueData[2] + " VS " + leagueData[3]
    }

    fun konkukDetail(eventData: EventData, date: String) {
        binding.textViewTitle.text = "🏫 "+eventData.event
    }

    fun KBODetail(eventData: EventData, date: String) {
        val kboData = eventData.event.split("/")
        binding.textViewTitle.text = "⚾️ " + kboData[1] + " VS " + kboData[2]

        binding.todoDetailTime.visibility = View.VISIBLE
        binding.textViewTime.text = date + kboData[0]

        binding.todoDetailStadium.visibility = View.VISIBLE
        binding.textViewStadium.text = kboData[4]

        binding.todoDetailChannel.visibility = View.VISIBLE
        binding.textViewChannel.text = kboData[3]


    }

    fun initBtn() {
        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
    }
}