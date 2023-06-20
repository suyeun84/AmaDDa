package com.example.amadda

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var itemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener: (Category) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val colorView: ImageView = itemView.findViewById<ImageView?>(R.id.categoryColorView)

        fun bind(category: Category) {
            textView.text = category.title
//            colorView.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            Log.d("wherestar", "${category.title} -> ${category.color}")

            if (category.color.length != 0) {
                colorView.drawable.setColorFilter(Color.parseColor(category.color), PorterDuff.Mode.SRC_IN)
//                colorView.setBackgroundColor(Color.parseColor(category.color))

            }
            itemView.setOnClickListener {
                itemClickListener?.invoke(category)
            }
        }
    }
}
