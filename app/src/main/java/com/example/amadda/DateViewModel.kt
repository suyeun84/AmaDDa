package com.example.amadda

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DateViewModel : ViewModel() {
    var curYear = MutableLiveData<Int>()
    var curMonth = MutableLiveData<Int>()

    fun setCurYear(data: Int) {
        curYear.value = data
        println("setCurYear $data")
    }

    fun setCurMonth(data: Int) {
        curMonth.value = data
        println("setCurMonth $data")
    }
}