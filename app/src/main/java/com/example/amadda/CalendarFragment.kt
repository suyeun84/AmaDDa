package com.example.amadda

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.amadda.databinding.FragmentCalendarBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        binding.apply {
            calendar.setTopbarVisible(false)
            calendar.setTitleFormatter(object : TitleFormatter {
                override fun format(calendarDay: CalendarDay): CharSequence {
                    // CalendarDay를 기반으로 포맷된 문자열을 반환하는 로직을 구현합니다.
                    // 원하는 형식으로 날짜를 포맷하고 반환합니다.
                    val month = calendarDay.month
                    val year = calendarDay.year

                    val str = "$year" + "년 " + "$month" + "월"
                    println(str)
                    ymtext.text = str

                    // 포맷된 문자열을 반환합니다.
                    return str
                }
            })

            // 주간 요일 텍스트를 한국어로 변경
            calendar.setWeekDayLabels(
                arrayOf("", "", "", "", "", "", "")
            )

            calendar.setOnDateChangedListener(object: OnDateSelectedListener {
                override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
                }
            })


        }

    }
}