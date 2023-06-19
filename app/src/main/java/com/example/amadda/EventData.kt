package com.example.amadda

data class EventData(
    val category: String,
    val event: String,
    val dDay: Int = 0,
    var edit: Boolean = false,
    var star: Boolean = false,
    var countNum: Int = 0
) {
    constructor(): this("", "", 0, false, false, 0) {

    }

}

