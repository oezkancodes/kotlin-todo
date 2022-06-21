package com.example.kotlintodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Todo

class TodoAdapter (private val context: Context, private val dataset: List<Todo>): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    /**
     * A ViewHolder represents a single list item view in RecyclerView, and can be reused when possible.
     * A ViewHolder instance holds references to the individual views within a list item layout.
     */
    class TodoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.todo_label)
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
            .inflate(R.layout.todo_item, parent, false)

        return TodoViewHolder(adapterLayout)
    }

    /**
     * This method is called by the layout manager in order to replace the contents of a list item view.
     */
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = dataset[position]
        holder.textView.text = item.stringResourceId.toString()
    }

    override fun getItemCount() = dataset.size
}