package com.example.amadda

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils.split
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.DayBinding

class CalendarRecyclerAdapter(val items: ArrayList<MyData>) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null

    var categoryArr: ArrayList<Category> = ArrayList()

    interface OnItemClickListener {
        fun OnClick(data: MyData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: DayBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.linearLayoutDay.setOnClickListener {
                itemClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
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
        if (position % 7 == 0) {
            holder.binding.textViewD.setTextColor(Color.RED)
        }
        if (position % 7 == 6) {
            holder.binding.textViewD.setTextColor(Color.BLUE)
        }
        if (items[position].date == "0") {
            holder.binding.textViewD.text = ""
            holder.binding.textViewTodo1.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.textViewTodo2.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.textViewTodo3.setBackgroundColor(Color.TRANSPARENT)
        } else if (items[position].date[6] == '0') {
            holder.binding.textViewD.text = items[position].date[7].toString()
        } else {
            holder.binding.textViewD.text = items[position].date.substring(6, 8)
        }

        if (items[position].count == 0) {
            holder.binding.textViewTodo1.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.textViewTodo2.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.textViewTodo3.setBackgroundColor(Color.TRANSPARENT)
        }


        if (items[position].count == 1) {
            Log.d("subArr", "adapter : ${items[position].event}")
            var bgColor = setColor(items[position].event[0].category)
            holder.binding.textViewTodo1.text = items[position].event[0].event
            holder.binding.textViewTodo1.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(bgColor))
            holder.binding.textViewTodo2.setBackgroundColor(Color.TRANSPARENT)
            holder.binding.textViewTodo3.setBackgroundColor(Color.TRANSPARENT)

        }

        if (items[position].count == 2) {
            var bgColor = ""
            for (i in 0..1) {
                bgColor = setColor(items[position].event[i].category)
                if (i == 0) {
                    holder.binding.textViewTodo1.text = items[position].event[i].event
                    holder.binding.textViewTodo1.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(bgColor))
                } else if (i == 1) {
                    holder.binding.textViewTodo2.text = items[position].event[i].event
                    holder.binding.textViewTodo2.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(bgColor))
                }
            }
            holder.binding.textViewTodo3.setBackgroundColor(Color.TRANSPARENT)
        }

        if (items[position].count >= 3) {
            var bgColor = ""
            for (i in 0..2) {
                bgColor = setColor(items[position].event[i].category)
                if (i == 0) {
                    holder.binding.textViewTodo1.text = items[position].event[i].event
                    holder.binding.textViewTodo1.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(bgColor))
                } else if (i == 1) {
                    holder.binding.textViewTodo2.text = items[position].event[i].event
                    holder.binding.textViewTodo2.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(bgColor))
                } else if (i == 2) {
                    holder.binding.textViewTodo3.text = items[position].event[i].event
                    holder.binding.textViewTodo3.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(bgColor))
                }
            }
        }

    }
    fun setColor(category: String): String {
        if (category == "konkuk") {
            return "#005426"
        } else if (category == "Premier") {
            return "#6300C7"
        } else if (category == "KBO") {
            return "#0047FF"
        } else if (category == "festival") {
            return "#FD9BFF"
        }
        else{
            for (i in categoryArr.indices) {
                if (category == categoryArr[i].title)
                    return categoryArr[i].color
            }
            return "#005426"
        }

    }
}