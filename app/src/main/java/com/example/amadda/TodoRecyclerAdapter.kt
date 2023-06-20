package com.example.amadda

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.TodoRowBinding

class TodoRecyclerAdapter(var items: ArrayList<EventData>, var likes: ArrayList<EventData>) :
    RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null
    var detailClickListener: OnItemClickListener? = null
    var checkClickListener: OnItemClickListener? = null

    var categoryArr: ArrayList<Category> = ArrayList()


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
            binding.imageViewTodoCheck.setOnClickListener {
                checkClickListener?.OnClick(items[adapterPosition], this, adapterPosition)
            }
        }
    }

    fun changeCategoryArr(ca: ArrayList<Category>) {
        categoryArr = ca
    }

    fun itemRenew(new: ArrayList<EventData>) {
        items.clear()
        items = new
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
            holder.binding.imageViewTodoStar.visibility = View.VISIBLE
            holder.binding.imageViewTodoCheck.visibility = View.GONE

            holder.binding.textViewTodoDetail.visibility = View.VISIBLE
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CC005426"))
            holder.binding.textViewTodoDetail.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#005426"))
            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = "건국대 학사일정"

        }else if (items[position].category == "KBO"){
            holder.binding.imageViewTodoStar.visibility = View.VISIBLE
            holder.binding.imageViewTodoCheck.visibility = View.GONE

            holder.binding.textViewTodoDetail.visibility = View.VISIBLE
            holder.binding.rowTodo.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F20047FF"))
            holder.binding.textViewTodoDetail.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#0047FF"))
//            holder.binding.textViewTodoTitle.text = items[position].tag
            val event = items[position].event.split(":")
            holder.binding.textViewTodoTitle.text = event[0]+" vs "+event[1]
//            val event = items[position].event.split("/")
//            holder.binding.textViewTodoTitle.text = event[1]+" vs "+event[2] + "\n" + event[0] + ","+ event[3] + ","+ event[4]
            holder.binding.textViewTodoCategory.text = "KBO리그"


        } else if (items[position].category == "Premier") {
            holder.binding.imageViewTodoStar.visibility = View.VISIBLE
            holder.binding.imageViewTodoCheck.visibility = View.GONE

            holder.binding.textViewTodoDetail.visibility = View.VISIBLE
            holder.binding.rowTodo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F26300C7"))
            holder.binding.textViewTodoDetail.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#6300C7"))
            holder.binding.textViewTodoTitle.text = items[position].event.split(":")[0] + " vs " + items[position].event.split(":")[1]
            holder.binding.textViewTodoCategory.text = "프리미어리그"


        } else if (items[position].category == "festival") {
            holder.binding.imageViewTodoStar.visibility = View.VISIBLE
            holder.binding.imageViewTodoCheck.visibility = View.GONE

            holder.binding.textViewTodoDetail.visibility = View.VISIBLE
            holder.binding.rowTodo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#F2FD9BFF"))
            holder.binding.textViewTodoDetail.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FD9BFF"))
            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = "페스티벌"
        }
        else if (items[position].category == "timetable") {
            holder.binding.rowTodo.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#000000"))
            holder.binding.textViewTodoDetail.visibility = View.GONE
            holder.binding.textViewTodoTitle.text = items[position].event
            var timeTableSplitData = items[position].extra.split(" ")
            holder.binding.textViewTodoCategory.text = timeTableSplitData[1] + " / " + timeTableSplitData[2] + "~" + timeTableSplitData[3]
        }
        else {

            holder.binding.textViewTodoTitle.text = items[position].event
            holder.binding.textViewTodoCategory.text = items[position].category
            holder.binding.textViewTodoDetail.visibility = View.GONE

            holder.binding.imageViewTodoStar.visibility = View.INVISIBLE
            holder.binding.imageViewTodoCheck.visibility = View.VISIBLE

            holder.binding.imageViewTodoCheck.isSelected = items[position].star

            for (i in categoryArr.indices) {
                if (categoryArr[i] != null) {
                    Log.d("categoryArr", "$categoryArr")
                    if (categoryArr[i].title == holder.binding.textViewTodoCategory.text) {
                        holder.binding.rowTodo.background.setColorFilter(Color.parseColor(categoryArr[i].color), PorterDuff.Mode.SRC_IN)
//                        colorView.drawable.setColorFilter(Color.parseColor(category.color), PorterDuff.Mode.SRC_IN)
                        break
                    }
                }
            }
        }

        holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_border_24)
        for (i in likes.indices) {
            if (likes[i].category == items[position].category && likes[i].code == items[position].code) {
                holder.binding.imageViewTodoStar.setImageResource(R.drawable.ic_baseline_star_24)
            }
        }
    }
}
