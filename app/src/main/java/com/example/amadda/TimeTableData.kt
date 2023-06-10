package com.example.amadda

class TimeTableData(
    val lecture: String,
    val professor: String,
    val place: String,
    val date: ArrayList<Int>,
    val startTime: String,
    val endTime: String
) : java.io.Serializable {
    constructor() : this("", "", "", ArrayList<Int>(), "", "") {
        // 생성자 내용 구현
    }
}