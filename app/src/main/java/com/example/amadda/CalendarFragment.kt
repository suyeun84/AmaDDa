package com.example.amadda

import android.app.AlertDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.FragmentCalendarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: FragmentCalendarBinding
    lateinit var adapter_calendar: CalendarRecyclerAdapter
    val monthData: ArrayList<MyData> = ArrayList()
    var todoList: ArrayList<String> = ArrayList()
    private var year = 2023
    private var month = 5
    val CALENDAR_EMPTY: String = "CALENDAR_EMPTY"
    val CALENDAR_DAY: String = "CALENDAR_DAY"
    val dateModel: DateViewModel by viewModels()
    var userId: String = ""


    private val konkukUrl =
        "http://www.konkuk.ac.kr/do/MessageBoard/HaksaArticleList.do?forum=11543"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId").toString()
        initDate()
        initCalendar()

    }

    private fun updateCalendar() {
        println("updateCalendar !!")
        monthData.clear()
        val calendar = GregorianCalendar(year, month, 1)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until dayOfWeek) {
            monthData.add(MyData(arrayListOf(), "0", arrayListOf()))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            monthData.add(MyData(arrayListOf(), mdate, arrayListOf()))
        }

        adapter_calendar.notifyDataSetChanged()

        getKonkukEvent()
        getKBO()

    }
    private fun initCalendar() {
        val calendar = GregorianCalendar(year, month, 1)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var str = ""

        for (i in 0 until dayOfWeek) {
            monthData.add(MyData(arrayListOf(), "0", arrayListOf()))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            monthData.add(MyData(arrayListOf(), mdate, arrayListOf()))
        }
        adapter_calendar = CalendarRecyclerAdapter(monthData)
        adapter_calendar.itemClickListener = object : CalendarRecyclerAdapter.OnItemClickListener {
            override fun OnClick(
                data: MyData,
                holder: CalendarRecyclerAdapter.ViewHolder,
                position: Int
            ) {
                val bundle = Bundle()
                bundle.putSerializable("data", data)
                val dialog: TodoFragment = TodoFragment()
                dialog.arguments = bundle

                requireActivity().supportFragmentManager.let { fragmentManager ->
                    dialog.show(
                        fragmentManager,
                        "TodoDialog"
                    )
                }

            }
        }
        adapter_calendar.notifyDataSetChanged()
        getKonkukEvent()
        getKBO()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        binding.apply {
            textView3.setText("$year" + "년 ${month + 1}" + "월")
            prevMonth.setOnClickListener {
                if (month == 0) {
                    year--
                    month = 11
                } else {
                    month--
                }
                dateModel.setCurYear(year)
                dateModel.setCurMonth(month)
                textView3.setText("$year" + "년 ${month + 1}" + "월")
                updateCalendar()

                println("prevMonth! $year / ${month + 1}")
            }
            nextMonth.setOnClickListener {
                if (month == 11) {
                    year++
                    month = 0
                } else {
                    month++
                }
                dateModel.setCurYear(year)
                dateModel.setCurMonth(month)
                textView3.setText("$year" + "년 ${month + 1}" + "월")
                updateCalendar()
                println("nextMonth! $year / ${month + 1}")
            }
        }

        return binding.root
//        val view =  inflater.inflate(R.layout.fragment_calendar, container, false)
//        return view
    }

    private fun initDate() {
        val date: LocalDate = LocalDate.now()
//        var iyear: Int = date.year
//        var imonth: Int = date.monthValue
        year = date.year
        month = date.monthValue
        dateModel.setCurYear(year)
        dateModel.setCurMonth(month)
        println("initdate! $year / $month")

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



    fun KBOConvertDate(input: String): String {
        // 숫자만 추출해서 월과 일로 분리
        val dateParts = input.filter { it.isDigit() || it == '.' }.split(".")
        val month = dateParts[0].toInt()
        val day = dateParts[1].toInt()

        // 현재 연도 사용
        val year = LocalDate.now().year

        // 날짜 객체 생성
        val date = LocalDate.of(year, month, day)

        // 원하는 형식으로 변환
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formattedDate = date.format(formatter)
        // 변환된 날짜 반환

        return formattedDate
    }


    private fun getKonkukEvent() {
        scope.launch {
            val konkukDoc = Jsoup.connect(konkukUrl).get()
            val name = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dd")
            val date = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dt")
            for (day in monthData) {
                day.name = arrayListOf()
                for (i in 0 until date.size) {
                    val convertedDate = convertDate(date[i].text())
                    if (day.date == convertedDate) {
                        day.category.add("konkuk")
                        day.name.add(name[i].text())
                        day.count += 1
                    }
                }
            }
            withContext(Dispatchers.Main) {
                adapter_calendar.notifyDataSetChanged()
            }
        }
    }

    private fun getKBO() {
        val KBOurl =
            "https://sports.news.naver.com/kbaseball/schedule/index?month=${month}&year=2023"

        scope.launch {
            val KBOdoc = Jsoup.connect(KBOurl).get()
            lateinit var dateKBO: String
            val monthlyMatch = KBOdoc.select("div#calendarWrap>div")
            for (dailyMatch in monthlyMatch) {
                var matchInfo: String = ""
                dateKBO = KBOConvertDate(dailyMatch.select("span.td_date").text())
                if (dailyMatch.select("span.td_hour").size != 1) {
                    val matches = dailyMatch.select("tbody>tr")
                    for (match in matches) {
                        val t1 = match.select("span.team_lft").text()
                        val t2 = match.select("span.team_rgt").text()
                        if (t1 == "SSG" || t2 == "SSG") {
                            matchInfo = listOf(
                                match.select("span.td_hour").text(),
                                t1,
                                t2,
                                match.select("span.td_stadium")[0].text(),
                                match.select("span.td_stadium")[1].text()
                            ).joinToString("/")
//                            Log.d("asdf", matchInfo)
                            for (i in 0 until monthData.size) {
                                var d = monthData[i]
                                if (d.date == "20230601") {
                                    Log.d("asdf", monthData[4].name.toString())
                                }

                                    if (d.date == dateKBO) {

//                                    Log.d("asdf", day.date)
                                    d.category.add("KBO")
                                    d.name.add(matchInfo)
                                    d.count += 1
                                    break
                                }
                            }
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
//        adapter_calendar = null
    }

}