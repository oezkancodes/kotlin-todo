package com.example.kotlintodo.data

import com.example.kotlintodo.model.Step
import com.example.kotlintodo.model.Todo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class Datasource {
    fun loadTodos(callback: (List<Todo>) -> Unit) {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val todoList: MutableList<Todo> = mutableListOf()

        /* Fetch data from Firebase Firestore */
        db.collection(user.currentUser!!.uid)
            .orderBy("done", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { docRef ->
                docRef.forEach {
                    val data: MutableMap<String, Any> = it.data

                    val uid = data["uid"] as String
                    val label = data["label"] as String
                    val note = data["note"] as String
                    val steps = data["steps"] as MutableList<Step>
                    val done = data["done"] as Boolean
                    val important = data["important"] as Boolean

                    /* Create instance and add into list */
                    val todo = Todo(
                        uid,
                        label,
                        note,
                        steps,
                        done,
                        important
                    )
                    todoList.add(todo)
                }
                callback(todoList)
            }
            .addOnFailureListener { exception -> println(exception) }
    }
}