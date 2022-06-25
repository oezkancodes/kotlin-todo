package com.example.kotlintodo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintodo.databinding.ActivityTodoDetailBinding
import com.example.kotlintodo.model.Step
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TodoDetailActivity : AppCompatActivity() {
    private lateinit var etAddStep: EditText
    private lateinit var uid: String
    private lateinit var binding: ActivityTodoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Get uid from intent.putExtra of previous Activity.
         */
        uid = intent.extras?.get("uid") as String
        loadTodo()

        /**
         * Setup Binding & View
         * TODO: rename binding IDs after UI is finished
         */
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val backButton = binding.btnBackToList

        val important = binding.favoriteStar
        val done = binding.cbCheckTodo
        val delete = binding.ivDeleteTodo

        backButton.setOnClickListener {
            super.onBackPressed()
        }

        delete.setOnClickListener {
            onDeleteTodo()
        }

        etAddStep = findViewById(R.id.etAddStep)

        etAddStep.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.d("Add Step Value:", "afterTextChanged $p0")
            }

        })
    }

    private fun loadTodo() {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        db.collection(user.currentUser!!.uid)
            .document(uid)
            .get()
            .addOnSuccessListener { docRef ->
                val data: MutableMap<String, Any> = docRef.data as MutableMap<String, Any>
                val label = data["label"] as String
                val note = data["note"] as String
                // TODO: set steps after UI is ready
                // val steps = data["steps"] as MutableList<Step>
                val done = data["done"] as Boolean
                val important = data["important"] as Boolean
                println(label)
                binding.cbCheckTodo.setText(label)
                binding.etNotes.setText(note)
                binding.cbCheckTodo.isChecked = done
                binding.favoriteStar.isChecked = important
            }
    }

    private fun onDeleteTodo() {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        db.collection(user.currentUser!!.uid)
            .document(uid)
            .delete()
            .addOnSuccessListener {
                showToast("Deleted Todo")
                startActivity(Intent(this, TodoListActivity::class.java))
            }
            .addOnFailureListener {
                showToast("Couldn't delete Todo")
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}