package com.example.amadda

import android.app.AlertDialog
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString("userId").toString()
        initDate()
        initCalendar()

    }

    private fun initCalendar() {
        val calendar = GregorianCalendar(year, month, 1)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var str = ""

        for (i in 0 until dayOfWeek) {
            monthData.add(MyData("0", todoList))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            monthData.add(MyData(mdate, todoList))
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
        getEvent()
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
            val name = doc.select("div.calendar_area > div.detail_calendar > dl > dd")
            val date = doc.select("div.calendar_area > div.detail_calendar > dl > dt")
            for (day in monthData) {
                day.name = arrayListOf()
                for (i in 0 until date.size) {
                    val convertedDate = convertDate(date[i].text())
                    if (day.date == convertedDate) {
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