package com.example.kotlintodo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintodo.databinding.ActivityTodoDetailBinding
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

        /**
         * Setup Binding & View
         */
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val backButton = binding.btnBackToList
        val important = binding.favoriteStar
        val done = binding.cbCheckTodo

        etAddStep = findViewById(R.id.etAddStep)

        etAddStep.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.d("Add Step Value:", "afterTextChanged $p0")
            }

        })
    }

    private fun loadTodo (uid: String) {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        db.collection(user.currentUser!!.uid)
            .document(uid)
            .get()
            .addOnSuccessListener { docRef ->
                val data: MutableMap<String, Any> = docRef.data as MutableMap<String, Any>

            }
    }
}