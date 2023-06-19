package com.example.amadda

import android.annotation.SuppressLint
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
        fun OnItemClick(data: EventData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: TodoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewTodoStar.setOnClickListener {
                itemClickListener?.OnItemClick(items[adapterPosition], this, adapterPosition)
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items[position].category == "konkuk"){
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#94CE95"))
            holder.binding.textViewTodoTitle.text = items[position].event
        }else if (items[position].category == "KBO"){
            val event = items[position].event.split(":")
            holder.binding.textViewTodoTitle.text = event[0]+" vs "+event[1]
//            val event = items[position].event.split("/")
//            holder.binding.textViewTodoTitle.text = event[1]+" vs "+event[2] + "\n" + event[0] + ","+ event[3] + ","+ event[4]
        }
        holder.binding.textViewTodoCategory.text = items[position].category

        if(items[position].star){
            holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_24)
        }else{
            holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_border_24)
        }
    }
}