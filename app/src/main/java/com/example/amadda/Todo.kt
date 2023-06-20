package com.example.amadda

import java.io.Serializable

data class Todo(
    var todoCategory: String,
    var date: String,
    var title: String,
    var done: Boolean
) : Serializable {
    constructor() : this("", "", "", false) {
    }
}
