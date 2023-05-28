package com.example.amadda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.amadda.databinding.ActivityLoginBinding
import com.example.amadda.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bnv: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bnv = binding.bottomNav

        init()
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
                        SubscribeFragment()
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
}