package com.example.amadda

import android.content.Context
import android.content.Intent
import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.amadda.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val scope = CoroutineScope(Dispatchers.IO)
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    val ImgArr = arrayListOf<Int>(R.drawable.todo, R.drawable.bookmark, R.drawable.subscribe, R.drawable.setting)
    var userId: String = ""

    private val konkukUrl =
        "http://www.konkuk.ac.kr/do/MessageBoard/HaksaArticleList.do?forum=11543"

    lateinit var rdb: DatabaseReference

    fun convertDate(dateString: String): String {
        // 공백 및 괄호 제거
        val cleanedString = dateString.replace(" ", "").replace("(", "").replace(")", "")

        // 점(.) 제거
        val dotRemovedString = cleanedString.replace(".", "")


        // 날짜를 분리
        val day = dotRemovedString.substring(0, 2)
        val month = dotRemovedString.substring(2, 4)
        val year = "2023"

        Log.d("adsf", "convertDate : $year$day$month")
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

    private fun saveEvent() {

        rdb = Firebase.database.getReference("Events/event")
        rdb.child("konkuk").get().addOnSuccessListener { _ ->
            val newArr = ArrayList<EventData>()
            scope.launch {
                val konkukDoc = Jsoup.connect(konkukUrl).get()
                val name = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dd")
                val date = konkukDoc.select("div.calendar_area > div.detail_calendar > dl > dt")
                for (i in 0 until date.size) {
                    Log.d("adsff", "${name[i].text()}: ${date[i].text()}, ")

                    var event: EventData = EventData("konkuk", name[i].text(),
                        0, false, false, 0,
                        convertDate(date[i].text()), i, ""
                    )
                    newArr.add(event)
//                    Log.d("adsff", "${name[i].text()}: ${date[i].text()}, ")
                }
                saveKonkukList(newArr) {success ->
                    if (success) {

                    } else {

                    }
                }
            }
//            withContext(Dispatchers.Main)
        }

        rdb.child("KBO").get().addOnSuccessListener { _ ->
            val newArr = ArrayList<EventData>()
            for (i in 4..7) {
                val KBOurl =
                    "https://sports.news.naver.com/kbaseball/schedule/index?month=${i + 1}&year=${2023}"

                scope.launch {
                    var j: Int = 0
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
                                    var eventStr = match.select("span.td_hour").text() + "\n" +
                                    match.select("span.td_stadium")[0].text() + "\n" +
                                    match.select("span.td_stadium")[1].text()

                                    var event: EventData = EventData("KBO", "$t1:$t2",
                                        0, false, false, 0,
                                        dateKBO, j,
                                        eventStr
                                    )
                                    j++
                                    newArr.add(event)
                                }
                            }
                        }
                    }
                    saveKBOList(newArr) {success ->
                        if (success) {

                        } else {

                        }
                    }
                }
            }
        }
    }

    private fun saveKonkukList(subArr: ArrayList<EventData>, completion: (Boolean) -> Unit) {
        rdb.child("konkuk").setValue(subArr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(true)
                } else {
                    completion(false)
                }
            }
    }

    private fun saveKBOList(subArr: ArrayList<EventData>, completion: (Boolean) -> Unit) {
        rdb.child("KBO").setValue(subArr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(true)
                } else {
                    completion(false)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav
        userId = intent.getStringExtra("userId").toString()

        init()
        // initLayout()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
    private fun init() {
//        saveEvent()
        val startFragment = CalendarFragment()
        var bundle = Bundle()
        Log.d("adsf", "init userId : $userId")
        bundle.putString("userId", userId)
        startFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, startFragment)
            .commitAllowingStateLoss()

        bnv.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.calendarMenu -> {
                        CalendarFragment()
                        // SettingFragment()
                    }
                    R.id.favoriteMenu -> {
                        BookMarkFragment()
                    }
                    R.id.subscribeMenu -> {
                        SubscribeFragment()
                    }
                    R.id.profileMenu -> {
                        SettingFragment()
                    }
                    else -> {
                         CalendarFragment()
//                        SettingFragment()
                    }
                }
            )
            true
        }
    }

    fun changeFragment(fragment: Fragment) {
        println(fragment.id)

        var bundle = Bundle()
        bundle.putString("userId", userId)
        var frg = fragment
        frg.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameLayout.id, frg)
            .commit()
    }

}
