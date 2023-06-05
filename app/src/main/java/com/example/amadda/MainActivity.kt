package com.example.amadda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.amadda.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    val ImgArr = arrayListOf<Int>(R.drawable.todo, R.drawable.bookmark, R.drawable.subscribe, R.drawable.setting)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav

        init()
        // initLayout()
    }

    private fun init() {
        supportFragmentManager.beginTransaction().replace(binding.frameLayout.id, CalendarFragment())
            .commitAllowingStateLoss()

        bnv.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.calendarMenu -> {
                        CalendarFragment()
                    }
                    R.id.favoriteMenu -> {
                        BookMarkFragment()
                    }
                    R.id.subscribeMenu -> {
                        SubscribeFragment()
                    }
                    else -> {
                        SubscribeFragment()
                    }
                }
            )
            true
        }
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frameLayout.id, fragment)
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