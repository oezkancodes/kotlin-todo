package com.example.kotlintodo.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.TodoDetailActivity
import com.example.kotlintodo.data.Datasource
import com.example.kotlintodo.model.Todo
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TodoAdapter(private val context: Context, private val dataset: List<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todoList = dataset

    /**
     * A ViewHolder represents a single list item view in RecyclerView, and can be reused when possible.
     * A ViewHolder instance holds references to the individual views within a list item layout.
     */
    class TodoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.todo_label)
        val checkboxDone: MaterialCheckBox = view.findViewById(R.id.todo_done)
        val checkboxImportant: MaterialCheckBox = view.findViewById(R.id.todo_important)
        val todoItem: MaterialCardView = view.findViewById(R.id.todo_item)
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
        val item = todoList[position]
        /**
         * Render item data
         */
        holder.textView.text = item.label
        holder.checkboxDone.isChecked = item.done
        holder.checkboxImportant.isChecked = item.important
        toggleDoneStrikethrough(holder, item.done)
        toggleImportantCheckboxColor(holder, item.important)

        holder.todoItem.setOnClickListener { view ->
            val intent = Intent(context, TodoDetailActivity::class.java)
            /**
             * Pass uid for TodoDetailActivity.
             * https://developer.android.com/reference/android/content/Intent#putExtra(java.lang.String,%20android.os.Parcelable)
             */
            intent.putExtra("uid", item.uid)
            context.startActivity(intent)
        }

        /**
         * Handle mark as done checkbox
         */
        holder.checkboxDone.setOnClickListener {
            val db = getTodoCollection()
            db
                .document(item.uid)
                .update("done", !item.done)
                .addOnSuccessListener {
                    if (item.done) showToast("Todo done")
                    else showToast("Todo reopened")
                    toggleDoneStrikethrough(holder, item.done)
                    updateTodoList()
                }
        }
        /**
         * Handle mark as important checkbox
         */
        holder.checkboxImportant.setOnClickListener {
            val db = getTodoCollection()
            db
                .document(item.uid)
                .update("important", !item.important)
                .addOnSuccessListener {
                    if (item.important) showToast("Todo marked")
                    else showToast("Todo unmarked")
                    toggleImportantCheckboxColor(holder, item.important)
                    updateTodoList()
                }
        }
    }

    /**
     * Get default data collection instance from uid of currentUser
     */
    private fun getTodoCollection(): CollectionReference {
        val user = FirebaseAuth.getInstance()
        val userId = user.currentUser!!.uid.toString()
        // TODO: check if currentUser does exist
        return Firebase.firestore.collection(userId)
    }

    private fun toggleImportantCheckboxColor(holder: TodoViewHolder, checked: Boolean) {
        holder.checkboxImportant.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                context,
                if (checked) R.color.primary else R.color.default_grey
            )
        )
    }

    private fun toggleDoneStrikethrough(holder: TodoViewHolder, done: Boolean) {
        holder.textView.paintFlags = if (done) Paint.STRIKE_THRU_TEXT_FLAG else 0
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun updateTodoList() {
        Datasource().loadTodos { dataset ->
            this.todoList = dataset
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = this.dataset.size
}