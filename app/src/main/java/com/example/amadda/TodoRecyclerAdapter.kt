package com.example.amadda

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.TodoRowBinding

class TodoRecyclerAdapter(val items: ArrayList<EventData>) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnClick(data: EventData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: TodoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewTodoStar.setOnClickListener {
                itemClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val View = TodoRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(View)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position].category == "konkuk"){
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#94CE95"))
        }
        holder.binding.textViewTodoCategory.text = items[position].category
        holder.binding.textViewTodoTitle.text = items[position].name
    }
}