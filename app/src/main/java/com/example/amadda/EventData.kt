package com.example.amadda

data class EventData(
    val category: String,
    val event: String,
    val Dday: Int,
    var edit: Boolean = false
){
    constructor() : this("pw","",0,false)
}
