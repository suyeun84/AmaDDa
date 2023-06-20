package com.example.amadda

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.FragmentCalendarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList
import kotlin.text.Typography.times

class CalendarFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: FragmentCalendarBinding
    lateinit var adapter_calendar: CalendarRecyclerAdapter
    val monthData: ArrayList<MyData> = ArrayList()

    val favoriteKBO : ArrayList<String> = arrayListOf("SSG", "롯데")
    val favoritePL : ArrayList<String> = arrayListOf("에버턴 FC", "루턴", "토트넘 홋스퍼 FC")

    //    var todoList: ArrayList<String> = ArrayList()
    private var year = 2023
    private var month = 5
    val dateModel: DateViewModel by viewModels()
    var userId: String = ""

    lateinit var rdb: DatabaseReference
    lateinit var subscribeArr: ArrayList<Int>

    private val konkukUrl =
        "http://www.konkuk.ac.kr/do/MessageBoard/HaksaArticleList.do?forum=11543"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //이거 못가져오는중
        userId = arguments?.getString("userId").toString()
        Log.d("adsf", userId)
        initDate()
        initCalendar()
        getSubscribe()
//        initCalendar()

    }

    private fun getSubscribe() {
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("subscribe").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Int>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        subscribeArr = subArr
                        Log.d("adsf", "subscribe count : ${subscribeArr.size}")
                        if (subscribeArr.contains(0)) {
                            getKonkukEvent2()
                        }
                        if (subscribeArr.contains(1)) {
                            getPremierLeague()
                        }
                        if (subscribeArr.contains(2)) {
                            getKBO2()
                        }
                    }
                }
            }
        }

    }

    private fun updateCalendar(arr: ArrayList<Int>) {
        println("updateCalendar !!")
        monthData.clear()
        val calendar = GregorianCalendar(year, month, 1)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until dayOfWeek) {
            monthData.add(MyData("0", arrayListOf()))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            monthData.add(MyData(mdate, arrayListOf()))
        }

        adapter_calendar.notifyDataSetChanged()

        if (subscribeArr.contains(0)) {
            getKonkukEvent2()
        }
        if (subscribeArr.contains(1)) {
            getPremierLeague()
        }
        if (subscribeArr.contains(2)) {
            getKBO2()
        }
    }

    private fun initCalendar() {
        println("initCalendar !!")
        val calendar = GregorianCalendar(year, month, 1)
        val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val max: Int = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var str = ""

        for (i in 0 until dayOfWeek) {
            monthData.add(MyData("0", arrayListOf()))
        }
        for (i in 1..max) {
            var mdate = Integer.toString(year * 10000 + (month + 1) * 100 + i)
            monthData.add(MyData(mdate, arrayListOf()))
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
                bundle.putString("userId", "kelsey6225")
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        userId = arguments?.getString("userId").toString()
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
                updateCalendar(subscribeArr)

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
                updateCalendar(subscribeArr)
                println("nextMonth! $year / ${month + 1}")
            }
        }

        return binding.root

    }

    private fun initDate() {
        val date: LocalDate = LocalDate.now()

        year = date.year
        month = date.monthValue - 1
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

    private fun getKonkukEvent2() {
        rdb = Firebase.database.getReference("Events/event")
        rdb.child("konkuk").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {

                            for (i in subArr.indices) {
                                if (subArr[i] != null && subArr[i].category=="konkuk") {
                                    if (day.date == subArr[i].date) {
                                        day.event.add(subArr[i])
                                        day.count += 1
                                    }
                                }
                            }
                        }
                    }
                    adapter_calendar.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getKBO2() {
        rdb = Firebase.database.getReference("Events/event")
        rdb.child("KBO").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {
                            for (i in subArr.indices) {
                                if (subArr[i] != null && subArr[i].category=="KBO") {
                                    if (day.date == subArr[i].date) {
                                        day.event.add(subArr[i])
                                        day.count += 1
                                    }
                                }
                            }
                        }
                    }
                    adapter_calendar.notifyDataSetChanged()
                }
            }
        }
    }



    private fun getKonkukEvent() {
        scope.launch {
            val konkukDoc = Jsoup.connect(konkukUrl).get()
            val name = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dd")
            val date = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dt")
            for (day in monthData) {
                for (i in 0 until date.size) {
                    val convertedDate = convertDate(date[i].text())
                    if (day.date == convertedDate) {
                        day.event.add(EventData("건국대 학사일정", name[i].text()))
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
            "https://sports.news.naver.com/kbaseball/schedule/index?month=${month + 1}&year=${year}"


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
                            for (i in 0 until monthData.size) {
                                var d = monthData[i]
                                if (d.date == dateKBO) {
                                    d.event.add(EventData("KBO리그",matchInfo))
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

    private fun getPremierLeague() {
        val PLurl =
            "https://www.zentoto.com/sports/soccer/epl/fixtures"

        scope.launch {
            val konkukDoc = Jsoup.connect(PLurl).get()
            val matches = konkukDoc.select("div.game-table div.league-game")

            for (match in matches) {
                val matchDetail = match.select("div.game>div")
                val dayList = matchDetail[0].select("p").text().split("-", " (", ")")
                val date = dayList[0] + dayList[1] + dayList[2]
                val time = dayList[3]
                val team1 = matchDetail[1].select("a.team-nm").text()
                val team2 = matchDetail[3].select("a.team-nm").text()

                if (favoritePL.contains(team1) || favoritePL.contains(team2)) {
                    for (day in monthData) {
                        if (day.date == date) {
                            var premierData = ""
                            premierData = listOf(
                                date,
                                time,
                                team1,
                                team2
                            ).joinToString("/")
                            var tag = team1 + ":" + team2
                            day.event.add(EventData("프리미어리그", premierData, tag))
                            day.count += 1
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
