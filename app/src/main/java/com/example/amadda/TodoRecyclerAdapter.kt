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
        }else if (items[position].category == "KBO"){
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CC0047FF"))
            holder.binding.textViewTodoDetail.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0047FF"))

            val event = items[position].event.split(":")
            holder.binding.textViewTodoTitle.text = event[0]+" vs "+event[1]
//            val event = items[position].event.split("/")
//            holder.binding.textViewTodoTitle.text = event[1]+" vs "+event[2] + "\n" + event[0] + ","+ event[3] + ","+ event[4]

        }
//        else if (items[position].category == "KBO리그"){
//            var KBOEventData = items[position].event.split("/")
//            holder.binding.textViewTodoTitle.text = KBOEventData[1] + " vs " + KBOEventData[2]
//
//        }
        holder.binding.textViewTodoCategory.text = items[position].category



        holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_border_24)
        for (i in likes.indices) {
            if (likes[i].category == items[position].category && likes[i].code == items[position].code) {
                holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_24)
            }
        }
    }
}
