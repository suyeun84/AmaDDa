package com.example.amadda

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.DayBinding
import com.example.amadda.databinding.TimetableRowBinding


class TimeTableAdapter(val items: ArrayList<TimeTableData>) :
    RecyclerView.Adapter<TimeTableAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null
    val dateArr: ArrayList<String> = ArrayList<String>()

    interface OnItemClickListener {
        fun OnClick(data: TimeTableData, holder: ViewHolder, position: Int)
    }

    inner class ViewHolder(val binding: TimetableRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.item.setOnClickListener {
                itemClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
            }
            dateArr.add("월")
            dateArr.add("화")
            dateArr.add("수")
            dateArr.add("목")
            dateArr.add("금")
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
        holder.binding.classRoom.text = items[position].place
        holder.binding.className.text = items[position].lecture
        var data =
            items[position].date.size
        holder.binding.textView1.text =
            dateArr[items[position].date[0]] + " " + items[position].startTime + " ~ " + items[position].endTime
        Log.d("timeTableeeee", data.toString())
        if (items[position].date.size == 2) {
            holder.binding.textView2.visibility = View.VISIBLE
            holder.binding.textView2.text =
                dateArr[items[position].date[1]] + " " + items[position].startTime + " ~ " + items[position].endTime

        }
    }
    fun removeItem(pos:Int){
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }
}