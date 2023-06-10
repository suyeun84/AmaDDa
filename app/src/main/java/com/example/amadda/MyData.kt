package com.example.amadda

class MyData(
    var date: String,
    var event : ArrayList<EventData>,
    var count: Int = 0
) : java.io.Serializable