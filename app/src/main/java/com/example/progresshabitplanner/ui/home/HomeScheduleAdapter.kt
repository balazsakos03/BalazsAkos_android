package com.example.progresshabitplanner.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progresshabitplanner.databinding.ItemHomeScheduleBinding
import com.example.progresshabitplanner.model.ScheduleResponse
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.progresshabitplanner.R


class HomeScheduleAdapter(
    private var items: List<ScheduleResponse>
) : RecyclerView.Adapter<HomeScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHomeScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvHabitTitle.text = item.habit.title
        holder.binding.tvTime.text = "${item.startTime ?: "-"} - ${item.endTime ?: "-"}"
        holder.binding.tvNotes.text = item.notes ?: ""
        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putInt("scheduleId", item.id)
            }
            view.findNavController()
                .navigate(R.id.action_homeFragment_to_scheduleDetailFragment, bundle)
        }
    }

    fun updateData(newItems: List<ScheduleResponse>) {
        items = newItems
        notifyDataSetChanged()
    }
}
