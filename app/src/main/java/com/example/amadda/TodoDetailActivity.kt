package com.example.amadda

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.content.Intent
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
        } else if (eventData.category == "Premier") {
            PLDetail(eventData, date)
        } else if (eventData.category == "festival") {
            festivalDetail(eventData, date)
        }
        binding.shareBtnAppbar.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_SEND)
            val eventData = "[ "+binding.textViewTitle.text.toString() +" ]\n"+ "일시 : "+binding.textViewYear.text.toString() + "년 "+binding.textViewDate.text.toString()
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, eventData)
            val chooser = Intent.createChooser(intent, eventData)
            startActivity(chooser)


    }


    }
    fun festivalDetail(eventData: EventData, date: String) {
        binding.todoDetailTime.visibility = View.VISIBLE
        val arr: List<String> = eventData.extra.split("_")
        binding.textViewTime.text = date + " " + arr[0]
        binding.textViewTitle.text = "🎉 " + eventData.event

        binding.todoDetailLocation.visibility = View.VISIBLE
        binding.textViewLocation.text = arr[1]

        binding.todoDetailTicket.visibility = View.VISIBLE
        binding.textViewTicket.text = arr[2]
        binding.textViewTicket.paintFlags = binding.textViewTicket.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        binding.textViewTicket.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.textViewTicket.text.toString()))
            startActivity(intent)
        }

    }
    fun PLDetail(eventData: EventData, date: String) {
//        val leagueData = eventData.event.split("/")
        binding.todoDetailTime.visibility = View.VISIBLE
        binding.textViewTime.text = date + " " + eventData.extra
        binding.textViewTitle.text = "⚽️ " + eventData.event.split(":")[0] + " VS " + eventData.event.split(":")[1]
    }

    fun konkukDetail(eventData: EventData, date: String) {
        binding.textViewTitle.text = "🏫 " + eventData.event
    }

//    fun KBODetail(eventData: EventData, date: String) {


    fun KBODetail(eventData: EventData, date: String) {
//        val kboData = eventData.event.split("/")
//        binding.textViewTitle.text = "⚾️ " + kboData[1] + " VS " + kboData[2]

        val kboData2 = eventData.extra.split("\n")
        Log.d("adsfffff", "$kboData2")


//        binding.imageViewIcon.setImageResource(R.drawable.ic_baseline_sports_baseball_24)

//        binding.textViewTitle.text = "⚾️ " + kboData[1] + " VS " + kboData[2]
        binding.textViewTitle.text = "⚾️ " +
                eventData.event.split(":")[0] + " VS " + eventData.event.split(":")[1]

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