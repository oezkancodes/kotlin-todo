package com.example.kotlintodo.data

import com.example.kotlintodo.model.Todo

class Datasource {
    fun loadTodos(): List<Todo> {
        return listOf<Todo>(
            Todo(1),
            Todo(2),
            Todo(3),
            Todo(4),
            Todo(5)
        )
    }
}