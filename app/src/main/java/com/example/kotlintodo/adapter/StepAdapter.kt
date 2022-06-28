package com.example.kotlintodo.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Step

class StepAdapter(
    private val context: Context,
    private val steps: MutableList<Step>
) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    var stepsList = steps

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepItemLabel: TextView = itemView.findViewById(R.id.etStepItem)
        val stepItemDelete: ImageView = itemView.findViewById(R.id.ivRemoveStep)
        val stepItemDone: CheckBox = itemView.findViewById(R.id.todo_detail_done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.step_list_step_item,
                parent,
                false
            )
        )
    }

    fun addStep(step: Step) {
        stepsList.add(step)
        notifyItemInserted(stepsList.size - 1)
    }

    private fun deleteStep(position: Int) {
        stepsList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        // Handle "done" property
        val isDone = stepsList[position].done
        holder.stepItemDone.isChecked = isDone
        toggleDoneStrikethrough(holder.stepItemLabel, isDone)

        val curStep = stepsList[position]
        holder.itemView.apply {
            holder.stepItemLabel.text = curStep.label
        }
        holder.stepItemDelete.setOnClickListener {
            deleteStep(position)
        }
        holder.stepItemDone.setOnClickListener {
            val isDone = holder.stepItemDone.isChecked
            stepsList[position].done = isDone
            toggleDoneStrikethrough(holder.stepItemLabel, isDone)
        }
    }

    private fun toggleDoneStrikethrough(textView: TextView, done: Boolean) {
        textView.paintFlags = if (done) Paint.STRIKE_THRU_TEXT_FLAG else 0
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }
}