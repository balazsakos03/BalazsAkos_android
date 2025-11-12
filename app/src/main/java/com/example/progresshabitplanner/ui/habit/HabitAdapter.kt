package com.example.progresshabitplanner.ui.habit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progresshabitplanner.R
import com.example.progresshabitplanner.model.HabitResponse

class HabitAdapter(private var habits: List<HabitResponse>) :
    RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvHabitName)
        val description: TextView = itemView.findViewById(R.id.tvHabitDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.name.text = habit.name
        holder.description.text = habit.description
    }

    override fun getItemCount(): Int = habits.size

    fun updateData(newHabits: List<HabitResponse>) {
        habits = newHabits
        notifyDataSetChanged()
    }
}
