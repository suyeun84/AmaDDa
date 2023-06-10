package com.example.amadda

data class EventData(
    val category: String,
    val name: String,
    val Dday: Int = 0,
    var edit: Boolean = false,
    var star: Boolean = false
) {

}

