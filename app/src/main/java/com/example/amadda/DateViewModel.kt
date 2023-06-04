package com.example.amadda

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DateViewModel : ViewModel() {
    val curYear = MutableLiveData<Int>()
    val curMonth = MutableLiveData<Int>()

    fun setCurYear(data: Int) {
        curYear.value = data
    }

    fun setCurMonth(data: Int) {
        curMonth.value = data
    }
}