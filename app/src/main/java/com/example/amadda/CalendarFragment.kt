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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.FragmentCalendarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.ArrayList
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.Date

class CalendarFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: FragmentCalendarBinding
    lateinit var adapter_calendar: CalendarRecyclerAdapter
    val monthData: ArrayList<MyData> = ArrayList()
    private var year = 2023
    private var month = 5
    val CALENDAR_EMPTY: String = "CALENDAR_EMPTY"
    val CALENDAR_DAY: String = "CALENDAR_DAY"
    val dateModel: DateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDate()
        initCalendar()

    }

    private fun initCalendar() {
        val calendar = GregorianCalendar(year, month, 1)
        Log.d("캘린더", calendar.get(Calendar.DAY_OF_WEEK).toString()) // 1일의 위치

        println("initCalendar running")
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var str = ""

        for (i in 0 until dayOfWeek) {
            monthData.add(MyData("0"))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            Log.d("mdate", mdate)
            monthData.add(MyData(mdate))
        }

        adapter_calendar = CalendarRecyclerAdapter(monthData)
        getEvent()
        println("inintCalendar finished!")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
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
    }

    fun convertDate(dateString: String): String {
        // 공백 및 괄호 제거
        val cleanedString = dateString.replace(" ", "").replace("(", "").replace(")", "")

        // 점(.) 제거
        val dotRemovedString = cleanedString.replace(".", "")

        // 날짜를 분리
        val day = dotRemovedString.substring(0, 2)
        val month = dotRemovedString.substring(2, 4)
        val year = "2023"

        // 변환된 날짜 반환
        return "$year$day$month"
    }

    private val konkukUrl =
        "http://www.konkuk.ac.kr/do/MessageBoard/HaksaArticleList.do?forum=11543"

    private fun getEvent() {
        scope.launch {
            val doc = Jsoup.connect(konkukUrl).get()
            Log.d("parsing입니다", doc.toString())
            val name = doc.select("div.calendar_area > div.detail_calendar > dl > dd")
            val date = doc.select("div.calendar_area > div.detail_calendar > dl > dt")
            Log.d("date입니다", date.toString())
            for (day in monthData) {
                for (i in 0 until date.size) {
                    Log.d("데이트", day.date)
                    val convertedDate = convertDate(date[i].text())
                    Log.d("일정", day.date + "," + convertedDate)
                    if (day.date == convertedDate) {
                        Log.d("이름", name[i].text())
                        day.name = name[i].text()
                    }
                }
            }
            withContext(Dispatchers.Main) {
                adapter_calendar.notifyDataSetChanged()
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("datemodel : ${dateModel.curYear.value} / ${dateModel.curMonth.value}")
        year = dateModel.curYear.value!!
        month = dateModel.curMonth.value!!

        binding.recyclerViewCalendar.setLayoutManager(
            StaggeredGridLayoutManager(
                7,
                StaggeredGridLayoutManager.VERTICAL
            )
        )
        binding.recyclerViewCalendar.adapter = adapter_calendar

    }
//    override suspend fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        // dateModel =
//
//        super.onViewCreated(view, savedInstanceState)
//        println("datemodel : ${dateModel.curYear.value} / ${dateModel.curMonth.value}")
//        year = dateModel.curYear.value!!
//        month = dateModel.curMonth.value!!

//        binding.recyclerViewCalendar.setLayoutManager(
//            StaggeredGridLayoutManager(
//                7,
//                StaggeredGridLayoutManager.VERTICAL
//            )
//        )
//        binding.recyclerViewCalendar.adapter = adapter_calendar
//    }

//        val calendar = GregorianCalendar(year, month, 1)
//
//        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 1일의 위치
//        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//
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
//        adapter_calendar = CalendarRecyclerAdapter(monthData)

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
//        adapter_calendar = null
    }

}