package com.example.amadda

import android.annotation.SuppressLint
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.FragmentBookMarkBinding
import com.example.amadda.databinding.FragmentBookMarkListBinding

class BookMarkAdapter(private var items: ArrayList<EventData>)  : RecyclerView.Adapter<BookMarkAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: EventData, position: Int)
    }

    var itemClickListener: OnItemClickListener?= null

    inner class ViewHolder (val binding: FragmentBookMarkBinding): RecyclerView.ViewHolder(binding.root){
        init {

            binding.eventrow.setOnClickListener {
                itemClickListener?.onItemClick(items[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FragmentBookMarkBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    //변화

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(items[position].category == "건국대 학사일정"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#005426"))
        }else if(items[position].category == "KBO리그"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#0047FF"))
        }else if(items[position].category == "프리미어리그"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#6300C7"))
        }
        holder.binding.category.text = items[position].category.toString()
        holder.binding.event.text = items[position].event.toString()
        holder.binding.Dday.text = "D-" + items[position].Dday.toString()
        holder.binding.Dday.text = "D-" + items[position].dDay.toString()
        if(items[position].edit){
            holder.binding.eraseBookMark.visibility = View.VISIBLE
        }else{
            holder.binding.eraseBookMark.visibility = View.GONE
        }
    }
//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(list: ArrayList<EventData>) {
//        items = list
//        notifyDataSetChanged()
//    }
}