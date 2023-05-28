package com.example.amadda

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.RowSubscribeBinding

class SubscribeAdapter(val items: ArrayList<Subscription>)
    : RecyclerView.Adapter<SubscribeAdapter.ViewHolder>() {

    interface OnItemClickListener{ // 완전히 독립적인 class를 만들 수 있게 함
        fun OnItemClick(data: Subscription, position: Int)
    }

    var itemClickListener:OnItemClickListener? = null


        inner class ViewHolder(val binding: RowSubscribeBinding) : RecyclerView.ViewHolder(binding.root) {
            init {
                binding.isSubscribing.setOnClickListener {
                    itemClickListener?.OnItemClick(items[adapterPosition], adapterPosition)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowSubscribeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {

            // subscriptionImage.setImageResource(R.drawable.subscribe_test)
            subscriptionImage.setImageResource(items[position].img)
            subscriptionTitle.text = items[position].name
            if (items[position].isSubscribing) {
                isSubscribing.text = "구독 중"
                isSubscribing.setTextColor(Color.BLACK)
                isSubscribing.setBackgroundResource(R.drawable.background_subscribe_selected)
            } else {
                isSubscribing.text = "구독하기"
                isSubscribing.setTextColor(Color.WHITE)
                isSubscribing.setBackgroundResource(R.drawable.background_subscribe_unselected)
            }
        }
    }
}