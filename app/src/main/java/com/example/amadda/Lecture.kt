package com.example.amadda

data class Lecture(
    val lecture: String,
    val professor: String,
    val place: String,
    val date: ArrayList<Int>,
    val startTime: String,
    val endTime: String
) {
    constructor() : this("", "", "", ArrayList<Int>(), "", "") {
        // 생성자 내용 구현
    }
}
