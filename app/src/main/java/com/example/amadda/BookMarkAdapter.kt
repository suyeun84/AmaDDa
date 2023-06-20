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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BookMarkAdapter(private var items: ArrayList<EventData>)  : RecyclerView.Adapter<BookMarkAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(data: EventData, position: Int)
    }

    var itemClickListener: OnItemClickListener?= null
    var eraseClickListener: OnItemClickListener?= null

    inner class ViewHolder (val binding: FragmentBookMarkBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.eraseBookMark.setOnClickListener {
                eraseClickListener?.onItemClick(items[adapterPosition], adapterPosition)
            }

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
        if(items[position].category == "konkuk"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#005426"))
            holder.binding.category.text = "건국대 학사일정"
        }else if(items[position].category == "KBO"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#0047FF"))
            holder.binding.category.text = "KBO 리그"
        }else if(items[position].category == "Premier"){
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#6300C7"))
            holder.binding.category.text = "프리미어리그"
        } else if (items[position].category == "festival") {
            holder.binding.Dday.setBackgroundColor(Color.parseColor("#FD9BFF"))
            holder.binding.category.text = "페스티벌"
        }

//        holder.binding.event.text = items[position].event.toString()

//        if (items[position].date.isNotEmpty()) {
//            val today = LocalDate.now()
//            Log.d("dateeee", "${items[position].date} !!")
//            val targetDate = LocalDate.of(items[position].date.toInt()/10000,
//                (items[position].date.toInt()%10000)/100,
//                items[position].date.toInt()%100) // 예: 2023년 12월 31일
//            Log.d("dateeee", "!!! $targetDate")
//
//            val dDay = ChronoUnit.DAYS.between(today, targetDate)
//
//
//            if (dDay >= 0) {
////            println("d-day: $dDay")
//                holder.binding.Dday.text = "D-$dDay"
//            } else {
//                holder.binding.Dday.text = "D+${-dDay}"
//            }
//        }

        holder.binding.event.text = items[position].event.toString()
        if(items[position].dDay == 0){
            holder.binding.Dday.text = "D-DAY"
        }else if(items[position].dDay in 1..99){
            holder.binding.Dday.text = "D-"+items[position].dDay.toString()
        } else if(items[position].dDay > 100){
            holder.binding.Dday.text = "D+"+(items[position].dDay.toInt()-100).toString()
        }

        if(items[position].edit){
            holder.binding.eraseBookMark.visibility = View.VISIBLE
        }else{
            holder.binding.eraseBookMark.visibility = View.GONE
        }
    }
//        notifyDataSetChanged()
}