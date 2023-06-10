package com.example.amadda

data class EventData(val category:String, val event:String, val Dday:Int, var edit:Boolean=false) {
    constructor() : this("", "", 0, false) {
        // 생성자 내용 구현
    }
}

