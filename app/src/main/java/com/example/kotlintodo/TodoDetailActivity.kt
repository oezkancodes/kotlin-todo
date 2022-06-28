package com.example.kotlintodo

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.adapter.StepAdapter
import com.example.kotlintodo.adapter.TodoAdapter
import com.example.kotlintodo.databinding.ActivityTodoDetailBinding
import com.example.kotlintodo.model.Step
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TodoDetailActivity : AppCompatActivity() {
    private lateinit var etAddStep: EditText
    private lateinit var uid: String
    private lateinit var binding: ActivityTodoDetailBinding
    private lateinit var stepAdapter: StepAdapter
    private lateinit var rvStepItems: RecyclerView

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
        val backButton = binding.todoDetailBack

        val important = binding.todoDetailImportant
        val done = binding.todoDetailDone
        val delete = binding.todoDetailDelete

        backButton.setOnClickListener {
            onUpdateTodo(callback = { startTodoListActivity() })
        }

        done.setOnClickListener {
            toggleDoneStrikethrough(done.isChecked)
        }

        delete.setOnClickListener {
            onDeleteTodo()
        }

        stepAdapter = StepAdapter(mutableListOf())

        rvStepItems = findViewById(R.id.rvStepItems)

        rvStepItems.adapter = stepAdapter

        rvStepItems.layoutManager = LinearLayoutManager(this)

        etAddStep = findViewById(R.id.todo_detail_add_step)

        etAddStep.setOnEditorActionListener { view, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                keyEvent == null ||
                keyEvent.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                //User finished typing
                val stepLabel = etAddStep.text.toString()
                if (stepLabel.isNotEmpty()) {
                    val step = Step("12345", stepLabel, false)
                    stepAdapter.addStep(step)
                    etAddStep.text.clear()
                }

                true
            }
            false
        }
    }

    private fun loadTodo() {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        db.collection(user.currentUser!!.uid)
            .document(uid)
            .get()
            .addOnSuccessListener { docRef ->
                val data: MutableMap<String, Any> = docRef.data as MutableMap<String, Any>

                // Extract data of each To-do Map
                val label = data["label"] as String
                val note = data["note"] as String
                val steps = data["steps"] as MutableList<HashMap<String, Any>>
                val done = data["done"] as Boolean
                val important = data["important"] as Boolean

                // Set UI
                binding.todoDetailLabel.setText(label)
                if (done) toggleDoneStrikethrough(true)
                binding.etNotes.setText(note)
                binding.todoDetailDone.isChecked = done
                binding.todoDetailImportant.isChecked = important

                // -- Loop steps and add them to view incrementally
                for (stepHashMap in steps) {
                    // Extract data of each Step HashMap
                    val uid = stepHashMap["uid"] as String
                    val label = stepHashMap["label"] as String
                    val done = stepHashMap["done"] as Boolean
                    val step = Step(uid, label, done)
                    stepAdapter.addStep(step)
                }
            }
            .addOnFailureListener {
                showToast("Couldn't load Todo")
                startActivity(Intent(this, TodoListActivity::class.java))
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

    private fun onUpdateTodo(callback: () -> Unit) {
        var user = FirebaseAuth.getInstance()
        val db = Firebase.firestore
        val data = hashMapOf(
            "label" to binding.todoDetailLabel.text.toString(),
            "note" to binding.etNotes.text.toString(),
            "steps" to stepAdapter.stepsList,
            "done" to binding.todoDetailDone.isChecked,
            "important" to binding.todoDetailImportant.isChecked
        )
        db.collection(user.currentUser!!.uid)
            .document(uid)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                showToast("Updated Todo")
                callback()
            }
            .addOnFailureListener {
                showToast("Couldn't update Todo")
                callback()
            }
    }

    private fun startTodoListActivity() {
        startActivity(Intent(this, TodoListActivity::class.java))
    }

    private fun toggleDoneStrikethrough(done: Boolean) {
        binding.todoDetailLabel.paintFlags = if (done) Paint.STRIKE_THRU_TEXT_FLAG else 0
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}