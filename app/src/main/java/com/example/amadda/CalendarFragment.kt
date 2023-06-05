package com.example.amadda

import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.FragmentCalendarBinding
import java.util.ArrayList

class CalendarFragment : Fragment() {
    var binding: FragmentCalendarBinding? = null
    var adapter_calendar: CalendarRecyclerAdapter? = null
    // val dateModel: DateViewModel by activityViewModels()
//    val dateModel = ViewModelProvider(this).get(DateViewModel::class.java)
//    val dateModel: DateViewModel by activityViewModels()
    val dateModel = ViewModelProvider(requireActivity()).get(DateViewModel::class.java)
    val monthData: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
<<<<<<< Updated upstream
//        val intent = Intent(context, CalendarActivity::class.java)
//        startActivity(intent)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val year: Int = dateModel.curYear.value!!
        val month: Int = dateModel.curMonth.value!!

        super.onViewCreated(view, savedInstanceState)
        binding!!.recyclerViewCalendar.setLayoutManager(
            StaggeredGridLayoutManager(
                7,
                StaggeredGridLayoutManager.VERTICAL
            )
=======
        return binding.root
//        val view =  inflater.inflate(R.layout.fragment_calendar, container, false)
//        return view
    }

    private fun initDate() {
        val date: LocalDate = LocalDate.now()
        val iyear: Int = date.year
        val imonth: Int = date.monthValue
        dateModel.setCurYear(iyear)
        dateModel.setCurMonth(imonth)
        println("initdate! $iyear / $imonth")

//        binding.prevMonth.setOnClickListener {
//            initCalendar()
//        }
//
//        binding.nextMonth.setOnClickListener {
//            initCalendar()
//            adapter_calendar = CalendarRecyclerAdapter(monthData)
//            binding.recyclerViewCalendar.adapter = adapter_calendar
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        println("datemodel : ${dateModel.curYear.value} / ${dateModel.curMonth.value}")
        year = dateModel.curYear.value!!
        month = dateModel.curMonth.value!!

        binding.recyclerViewCalendar.layoutManager = StaggeredGridLayoutManager(
            7,
            StaggeredGridLayoutManager.VERTICAL
>>>>>>> Stashed changes
        )
//        val calendar = GregorianCalendar(year, month, 1)

        //여기 코드 실행하면 7월꺼 출력됨
//        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 1일의 위치
//        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

<<<<<<< Updated upstream
//        var str = ""

//        binding!!.textViewDate.setText(
//            Integer.toString(calendar.get(Calendar.YEAR)) + "/" + Integer.toString(
//                calendar.get(Calendar.MONTH) + 1
//            )
//        )
        for (i in 0 until dayOfWeek) {
            monthData.add("0")
//            adapter_calendar.addItem(CALENDAR_EMPTY, 0, false, 0)
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            Log.d("mdate", mdate)
//            var cursor_calendar_schedule: Cursor? = null
//            cursor_calendar_schedule = sqliteDB.rawQuery(
//                "SELECT * FROM SCHEDULE WHERE DATE = '" + mdate.toString() + "'",
//                null
//            )
//            str += Integer.toString(cursor_calendar_schedule.count) + " "
//            if (cursor_calendar_schedule.count != 0) {
//                adapter_calendar.addItem(CALENDAR_DAY, i, true, cursor_calendar_schedule.count)
//            } else {
//                adapter_calendar.addItem(CALENDAR_DAY, i, false, 0)
//            }
            monthData.add(mdate)
//            adapter_calendar.addItem(CALENDAR_DAY, i, false, 0)

            //날짜에 따른 일정 표시
        }
=======
//        for (i in 0 until dayOfWeek) {
//            monthData.add("0")
//        }
//        for (i in 1..max) {
//            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
//            Log.d("mdate", mdate)
//
//            monthData.add(mdate)
//            //날짜에 따른 일정 표시
//        }
>>>>>>> Stashed changes
        adapter_calendar = CalendarRecyclerAdapter(monthData)
        binding!!.recyclerViewCalendar.adapter = adapter_calendar

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter_calendar = null
    }

}