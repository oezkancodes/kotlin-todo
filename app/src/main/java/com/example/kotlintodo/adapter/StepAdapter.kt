package com.example.kotlintodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Step

class StepAdapter(
    private val steps: MutableList<Step>
) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepItemLabel: TextView = itemView.findViewById(R.id.tvStepItem)
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
        steps.add(step)
        notifyItemInserted(steps.size - 1)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val curStep = steps[position]
        holder.itemView.apply {
            holder.stepItemLabel.text = curStep.label
        }
    }

    override fun getItemCount(): Int {
        return steps.size
    }
}