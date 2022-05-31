package com.example.kotlintodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlintodo.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    private fun login (email: String, password: String) {
        /**
         * Basic input validation
         */
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this,  "Please enter E-Mail and Password.", Toast.LENGTH_SHORT).show()
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
                        val user: FirebaseUser = task.result!!.user!!
                        println("LOGIN SUCCESSFUL")
                        println("USER: " + user.email)
                    } else {
                        println("LOGIN ERROR")
                        Toast.makeText(this,  "Invalid Login Data. Check E-Mail and Password.", Toast.LENGTH_SHORT).show()
                    }
                }
            )
    }
}