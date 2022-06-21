package com.example.kotlintodo.data

import com.example.kotlintodo.model.Step
import com.example.kotlintodo.model.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Datasource {
    fun loadTodos(): List<Todo> {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val todoList: MutableList<Todo> = mutableListOf()
        db.collection(user.currentUser!!.uid)
            .get()
            .addOnSuccessListener { docRef ->
                docRef.forEach {
                    val data = it.getData()
                    todoList.add(
                        Todo(
                            uid = "uid",
                            label = "Label",
                            note = "Note",
                            steps = mutableListOf<Step>(),
                            done = false,
                            important = false
                        )
                    )
                    println(data)
                }
            }
            .addOnFailureListener { exception -> println(exception) }

        println(todoList)
        return todoList
    }
}