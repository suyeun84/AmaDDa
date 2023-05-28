package com.example.amadda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amadda.databinding.ActivityLoginBinding
import com.example.amadda.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val ImgArr = arrayListOf<Int>(R.drawable.todo, R.drawable.bookmark, R.drawable.subscribe, R.drawable.setting)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout(){
        binding.viewpager.adapter = MyViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager){
            tab, pos ->
            tab.setIcon(ImgArr[pos])
        }.attach()
    }
}