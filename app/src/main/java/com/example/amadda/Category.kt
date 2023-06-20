package com.example.amadda

data class Category(var title: String, var color: String) : java.io.Serializable {
    constructor() : this("", "")
}
