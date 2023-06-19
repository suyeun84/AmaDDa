package com.example.amadda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.amadda.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    val ImgArr = arrayListOf<Int>(R.drawable.todo, R.drawable.bookmark, R.drawable.subscribe, R.drawable.setting)
    var userId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav
        userId = intent.getStringExtra("userId").toString()

        init()
        // initLayout()
    }

    private fun init() {
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

//    private fun initLayout(){
//        binding.viewpager.adapter = MyViewPagerAdapter(this)
//        TabLayoutMediator(binding.tabLayout, binding.viewpager){
//                tab, pos ->
//            tab.setIcon(ImgArr[pos])
//        }.attach()
//    }
}


/*

    fun initBtn(){
        binding.buttonTodo.setOnClickListener {
            TodoFragment().show(
                supportFragmentManager, "SampleDialog"
            )
        }
        binding.buttonAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.buttonCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)        }
    }}
 */