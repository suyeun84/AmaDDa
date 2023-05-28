package com.example.amadda

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.FragmentBookMarkBinding

class BookMarkAdapter(private val items: List<EventData>)  : RecyclerView.Adapter<BookMarkAdapter.ViewHolder>() {

    interface OnItemClickListener {
    }

    var itemClickListener: OnItemClickListener?= null

    inner class ViewHolder (val binding: FragmentBookMarkBinding): RecyclerView.ViewHolder(binding.root){
        init {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.category.text = items[position].category.toString()
        holder.binding.event.text = items[position].event.toString()
        holder.binding.Dday.text = "D-" + items[position].Dday.toString()
    }
}