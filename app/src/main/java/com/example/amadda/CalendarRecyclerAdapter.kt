package com.example.amadda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.DayBinding

class CalendarRecyclerAdapter(val items: ArrayList<String>) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnClick(year: Int, month: Int)
    }

    inner class ViewHolder(val binding: DayBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.linearLayoutDay.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val View = DayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(View)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position] == "0"){
            holder.binding.textViewD.text = ""
        }else if (items[position][6] == '0'){
            holder.binding.textViewD.text = items[position][7].toString()
        }else{
            holder.binding.textViewD.text = items[position].substring(6,8)
        }
    }
}