package com.example.amadda

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.DayBinding
import com.example.amadda.databinding.TimetableRowBinding


class TimeTableAdapter(val items: ArrayList<TimeTableData>) :
    RecyclerView.Adapter<TimeTableAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnClick(data: TimeTableData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: TimetableRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.item.setOnClickListener {
                itemClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val View = TimetableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(View)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun addItem(item: TimeTableData) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.classRoom.text = items[position].classRoom
        holder.binding.className.text =items[position].className

    }
}