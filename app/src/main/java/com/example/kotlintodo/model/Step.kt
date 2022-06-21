package com.example.kotlintodo.model

data class Step(
    val uid: String,
    var label: String,
    var done: Boolean
)