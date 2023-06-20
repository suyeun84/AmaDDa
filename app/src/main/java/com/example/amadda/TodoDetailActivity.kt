package com.example.amadda

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
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

        if (eventData.category == "KBO") {
            KBODetail(eventData, date)
        } else if (eventData.category == "konkuk") {
            konkukDetail(eventData, date)
        }


    }

    fun konkukDetail(eventData: EventData, date: String) {
        binding.textViewTitle.text = eventData.event
    }


    fun KBODetail(eventData: EventData, date: String) {
        val kboData = eventData.event.split("/")

        val kboData2 = eventData.extra.split("\n")
        Log.d("adsfffff", "$kboData2")



        binding.imageViewIcon.setImageResource(R.drawable.ic_baseline_sports_baseball_24)

//        binding.textViewTitle.text = kboData[1] + " VS " + kboData[2]
        binding.textViewTitle.text = eventData.event.split(":")[0] + " VS " + eventData.event.split(":")[1]

        binding.todoDetailTime.visibility = View.VISIBLE
//        binding.textViewTime.text = date + kboData[0]
        binding.textViewTime.text = date + kboData2[0]

        binding.todoDetailStadium.visibility = View.VISIBLE
//        binding.textViewStadium.text = kboData[4]
        binding.textViewStadium.text = kboData2[2]

        binding.todoDetailChannel.visibility = View.VISIBLE
//        binding.textViewChannel.text = kboData[3]
        binding.textViewChannel.text = kboData2[1]


    }

    fun initBtn() {
        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
    }
}