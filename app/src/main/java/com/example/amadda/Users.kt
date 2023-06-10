package com.example.amadda

data class Users(
    var id: String,
    var password: String,
    var subscribe: ArrayList<Int>?,
    var todo: ArrayList<Todo>?,
    var category: ArrayList<Category>?
) {
    constructor() : this("id", "pw", ArrayList<Int>(), ArrayList<Todo>(), ArrayList<Category>())
}
