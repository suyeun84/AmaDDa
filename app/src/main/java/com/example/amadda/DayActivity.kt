package com.example.amadda

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.amadda.databinding.ActivityDayBinding


class DayActivity : AppCompatActivity() {
    var txtText: TextView? = null
    lateinit var binding: ActivityDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityDayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //데이터 가져오기
//        val intent = intent
//        val data = intent.getStringExtra("data")
//        txtText!!.text = data
    }

//    //확인 버튼 클릭
//    fun mOnClose(v: View?) {
//        //데이터 전달하기
//        val intent = Intent()
//        intent.putExtra("result", "Close Popup")
//        setResult(RESULT_OK, intent)
//
//        //액티비티(팝업) 닫기
//        finish()
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        //바깥레이어 클릭시 안닫히게
//        return if (event.action == MotionEvent.ACTION_OUTSIDE) {
//            false
//        } else true
//    }
//
//    override fun onBackPressed() {
//        //안드로이드 백버튼 막기
//        return
//    }
}