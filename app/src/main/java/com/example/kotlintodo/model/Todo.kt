package com.example.kotlintodo.model

data class Todo(
    val uid: String,
    var label: String,
    var note: String,
    var steps: MutableList<Step>,
    var done: Boolean,
    var important: Boolean
)