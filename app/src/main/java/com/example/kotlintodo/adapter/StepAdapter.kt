package com.example.kotlintodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlintodo.R
import com.example.kotlintodo.model.Step

class StepAdapter(
    private val steps: MutableList<Step>
) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {

    var stepsList = steps

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepItemLabel: TextView = itemView.findViewById(R.id.etStepItem)
        val stepItemDelete: ImageView = itemView.findViewById(R.id.ivRemoveStep)
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
        val curStep = stepsList[position]
        holder.itemView.apply {
            holder.stepItemLabel.text = curStep.label
        }
        holder.stepItemDelete.setOnClickListener{
            deleteStep(position)
        }
    }

    override fun getItemCount(): Int {
        return stepsList.size
    }
}