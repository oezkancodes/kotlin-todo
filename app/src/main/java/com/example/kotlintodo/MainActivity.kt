package com.example.kotlintodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlintodo.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Check if user exists
         */
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Toast.makeText(
                this,
                "Welcome back!",
                Toast.LENGTH_SHORT
            ).show()
            goToTasks()
            return
        }

        /**
         * Set Activity view with specific layout
         */
        setContentView(R.layout.activity_main)

        /**
         * Input bindings
         */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val login = binding.login

        login.setOnClickListener {
            login(email.text.toString(), password.text.toString())
        }
    }

    /**
     * Login user with E-Mail and Password.
     */
    private fun login(email: String, password: String) {
        /**
         * Basic input validation
         */
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please enter E-Mail and Password.", Toast.LENGTH_SHORT).show()
            return
        }

        /**
         * Firebase Authentication
         */
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        // val user: FirebaseUser = task.result!!.user!!
                        goToTasks()
                    } else {
                        Toast.makeText(
                            this,
                            "Invalid Login Data. Check E-Mail and Password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }

    private fun goToTasks() {
        startActivity(Intent(this, TasksActivity::class.java))
    }
}