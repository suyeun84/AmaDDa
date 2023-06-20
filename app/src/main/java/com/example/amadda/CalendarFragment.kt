package com.example.amadda

import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationCompat.getCategory
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: FragmentCalendarBinding
    lateinit var adapter_calendar: CalendarRecyclerAdapter
    val monthData: ArrayList<MyData> = ArrayList()

    lateinit var kboTeamArr: Array<String>
    lateinit var preTeamArr: Array<String>

    private var year = 0
    private var month = 0
    val dateModel: DateViewModel by viewModels()
    var userId: String = ""

    lateinit var rdb: DatabaseReference
    lateinit var subscribeArr: ArrayList<Int>

    val timeTableArr: ArrayList<TimeTableData> = ArrayList()

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

    private fun getCategory() {
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Category>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        adapter_calendar.categoryArr = subArr
                    }
                }
            }
        }
    }

    private fun getSubscribe() {
        getTimeTable()
        kboTeamArr = resources.getStringArray(R.array.kbo_team)
        preTeamArr = resources.getStringArray(R.array.primier_team)
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("subscribe").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Int>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        subscribeArr = subArr
                        getTodoEvent()
                        Log.d("adsf", "subscribe count : ${subscribeArr.size}")
                        if (subscribeArr.contains(0)) {
                            getKonkukEvent2()
                        }
                        if (subscribeArr.any { it in 1..11 }) {
                            val filteredList = ArrayList<String>()

                            for (number in subscribeArr) {
                                if (number in 1..11) {
                                    filteredList.add(preTeamArr[number - 1])
                                }
                            }
                            getPremier(filteredList)
                        }
                        if (subscribeArr.any { it in 12..21 }) {
                            val filteredList = ArrayList<String>()

                            for (number in subscribeArr) {
                                if (number in 12..21) {
                                    filteredList.add(kboTeamArr[number - 12])
                                }
                            }
                            getKBO2(filteredList)
                        }
                        if (subscribeArr.contains(22)) {
                            getFestivalEvent()

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
        getCategory()
        getTimeTable()
        getTodoEvent()
        if (subscribeArr.contains(0)) {
            getKonkukEvent2()
        }
        if (subscribeArr.any { it in 1..11 }) {
            val filteredList = ArrayList<String>()

            for (number in subscribeArr) {
                if (number in 1..11) {
                    filteredList.add(preTeamArr[number - 1])
                }
            }
            getPremier(filteredList)
        }
        if (subscribeArr.any { it in 12..21 }) {
            val filteredList = ArrayList<String>()

            for (number in subscribeArr) {
                if (number in 12..21) {
                    filteredList.add(kboTeamArr[number - 12])
                }
            }
            getKBO2(filteredList)
        }
        if (subscribeArr.contains(22)) {
            getFestivalEvent()

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
                if (holder.binding.textViewD.text.toString() != "") {
                    val bundle = Bundle()
                    bundle.putSerializable("data", data)
                    bundle.putString("userId", userId)
                    bundle.putString("year", year.toString())
//                bundle.putSerializable("category", )
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

    private fun dateToDay(date: String): Int {
        if (date == "0") {
            return -1
        }
        val year = date.substring(0, 4)
        val month = date.substring(4, 6)
        val day = date.substring(6, 8)

        val dateString = "$year-$month-$day"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val date = LocalDate.parse(dateString, formatter)
        val dayOfWeek = date.dayOfWeek.value
        return dayOfWeek - 1
    }

    private fun getTimeTable() {
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("timetableList").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<TimeTableData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {
                            Log.d("day", dateToDay(day.date).toString())
                            for (i in subArr.indices) {
                                if (subArr[i].date.contains(dateToDay(day.date))) {
                                    var timeTableData = EventData("timetable", subArr[i].lecture)
                                    timeTableData.extra = listOf(
                                        subArr[i].lecture,
                                        subArr[i].place,
                                        subArr[i].startTime,
                                        subArr[i].endTime
                                    ).joinToString(" ")
                                    day.event.add(timeTableData)
                                    day.count += 1
                                }
                            }
                        }
                    }
                    adapter_calendar.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getFestivalEvent() {
        rdb = Firebase.database.getReference("Events/event")
        rdb.child("festival").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {
                            for (i in subArr.indices) {
                                if (subArr[i] != null && subArr[i].category=="festival") {
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

    private fun getTodoEvent() {
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoList").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {
                            for (i in subArr.indices) {
                                if (subArr[i] != null) {
                                    if (day.date == subArr[i].date) {
                                        day.event.add(subArr[i])
                                        day.count += 1
                                        EventBus.getDefault().post(ToDoEvent(day.event))
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

    private fun getKBO2(nowList: ArrayList<String>) {
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
                                    if (nowList.contains(subArr[i].event.split(":")[0])
                                        || nowList.contains(subArr[i].event.split(":")[1])) {
                                        if (day.date == subArr[i].date) {
                                            day.event.add(subArr[i])
                                            day.count += 1
                                        }
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

    private fun getPremier(nowList: ArrayList<String>) {
        rdb = Firebase.database.getReference("Events/event")
        rdb.child("Premier").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (day in monthData) {
                            for (i in subArr.indices) {
                                if (subArr[i] != null && subArr[i].category=="Premier") {
                                    if (nowList.contains(subArr[i].event.split(":")[0])
                                        || nowList.contains(subArr[i].event.split(":")[1])) {
                                        if (day.date == subArr[i].date) {
                                            day.event.add(subArr[i])
                                            day.count += 1
                                        }
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

        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Category>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        adapter_calendar.categoryArr = subArr
                        binding.recyclerViewCalendar.adapter = adapter_calendar
                    }
                }
                else {
                    val subArr = ArrayList<Category>()
                    adapter_calendar.categoryArr = subArr
                    binding.recyclerViewCalendar.adapter = adapter_calendar
                }
            }

        }




    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
//        adapter_calendar = null
    }

    override fun onStart() {
        super.onStart()
        Log.d("subArr", "onStart")
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d("subArr", "onStop")
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printId(event: BusEvent) {
//        Log.d("subArr", "")
        if (event.id == 1) {
            Log.d("subArr", "received")
            Toast.makeText(context, "${event.id}", Toast.LENGTH_SHORT).show()
            updateCalendar(subscribeArr)
        }
    }

}
