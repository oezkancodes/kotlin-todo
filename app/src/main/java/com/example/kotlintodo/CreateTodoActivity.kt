package com.example.kotlintodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlintodo.databinding.ActivityCreateTodoBinding
import com.example.kotlintodo.model.Step
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateTodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val label = binding.inputCreateTodoLabel
        val createButton = binding.btnCreateTodo
        val backButton = binding.btnCancelCreateTodo

        createButton.setOnClickListener{
            onCreateTodo(label.text.toString())
        }
        backButton.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun onCreateTodo(label: String) {
        if (label.isEmpty()) {
            showToast("Please enter Todo label")
            return
        }


        var user = FirebaseAuth.getInstance()
        var db = Firebase.firestore
        var data = HashMap<String, Any>()
        val todoRef = db.collection(user.currentUser!!.uid.toString()).document()

        data = hashMapOf(
            "uid" to todoRef.id,
            "label" to label,
            "note" to "",
            "steps" to arrayListOf<Step>(),
            "done" to false,
            "important" to false
        )

        todoRef
            .set(data)
            .addOnSuccessListener {
                showToast("Created new Todo")
                startActivity(Intent(this, TodoListActivity::class.java))
            }
            .addOnFailureListener {
                showToast("Failed to create Todo")
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}