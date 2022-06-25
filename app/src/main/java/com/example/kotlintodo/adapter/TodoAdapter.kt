package com.example.kotlintodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Todo
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TodoAdapter(private val context: Context, private val dataset: List<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    /**
     * A ViewHolder represents a single list item view in RecyclerView, and can be reused when possible.
     * A ViewHolder instance holds references to the individual views within a list item layout.
     */
    class TodoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.todo_label)
        val checkboxDone: MaterialCheckBox = view.findViewById(R.id.todo_done)
        val checkboxImportant: MaterialCheckBox = view.findViewById(R.id.todo_important)
    }

    /**
     * Is called by the layout manager to create new view holders for the RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        /**
         * Create new view.
         */
        val adapterLayout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.todo_list_todo_item, parent, false)

        return TodoViewHolder(adapterLayout)
    }

    /**
     * This method is called by the layout manager in order to replace the contents of a list item view.
     */
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = dataset[position]
        /**
         * Render item data
         */
        holder.textView.text = item.label
        holder.checkboxDone.isChecked = item.done
        holder.checkboxImportant.isChecked = item.important

        /**
         * Handle mark as done checkbox
         */
        holder.checkboxDone.setOnClickListener {
            val db = getTodoCollection()
            db
                .document(item.uid)
                .update("done", !item.done)
                .addOnSuccessListener {
                    item.done = !item.done
                    holder.checkboxDone.isChecked = item.done
                    if (item.done) showToast("Todo done")
                    if (!item.done) showToast("Todo reopened")
                }
        }
        /**
         * Handle mark as important checkbox
         */
        holder.checkboxImportant.setOnClickListener{
            val db = getTodoCollection()
            db
                .document(item.uid)
                .update("important", !item.important)
                .addOnSuccessListener {
                    item.important = !item.important
                    holder.checkboxImportant.isChecked = item.important
                    if (item.important) showToast("Todo marked")
                    if (!item.important) showToast("Todo unmarked")
                }
        }
    }

    /**
     * Get default data collection instance from uid of currentUser
     */
    private fun getTodoCollection (): CollectionReference {
        val user = FirebaseAuth.getInstance()
        val userId = user.currentUser!!.uid.toString()
        // TODO: check if currentUser does exist
        return Firebase.firestore.collection(userId)
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount() = dataset.size
}