package com.example.amadda

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return TodoFragment()
            1 -> return BookMarkFragment()
            2 -> return SubscribeFragment()
            3 -> return SettingFragment()
            else -> return TodoFragment()
        }
    }
}