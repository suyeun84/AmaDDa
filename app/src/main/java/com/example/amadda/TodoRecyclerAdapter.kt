package com.example.amadda

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.TodoRowBinding

class TodoRecyclerAdapter(val items: ArrayList<EventData>, var likes: ArrayList<EventData>) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null
    var detailClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnClick(data: EventData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: TodoRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageViewTodoStar.setOnClickListener {
                itemClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
            }
            binding.textViewTodoDetail.setOnClickListener {
                detailClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
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
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CC005426"))
            holder.binding.textViewTodoDetail.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#005426"))
            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = "건국대 학사일정"

        }else if (items[position].category == "KBO"){
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F20047FF"))
            holder.binding.textViewTodoDetail.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0047FF"))
//            holder.binding.textViewTodoTitle.text = items[position].tag
            val event = items[position].event.split(":")
            holder.binding.textViewTodoTitle.text = event[0]+" vs "+event[1]
//            val event = items[position].event.split("/")
//            holder.binding.textViewTodoTitle.text = event[1]+" vs "+event[2] + "\n" + event[0] + ","+ event[3] + ","+ event[4]
            holder.binding.textViewTodoCategory.text = "KBO리그"


        } else if (items[position].category == "Premier") {
            holder.binding.rowTodo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F26300C7"))
            holder.binding.textViewTodoDetail.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#6300C7"))
            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = "프리미어리그"


        } else if (items[position].category == "festival") {
            holder.binding.rowTodo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F2FD9BFF"))
            holder.binding.textViewTodoDetail.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FD9BFF"))
            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = "페스티벌"
        }
        else {
            holder.binding.textViewTodoTitle.text = items[position].event
        }

        holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_border_24)
        for (i in likes.indices) {
            if (likes[i].category == items[position].category && likes[i].code == items[position].code) {
                holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_24)
            }
        }
    }
}
