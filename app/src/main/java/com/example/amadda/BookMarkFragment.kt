package com.example.amadda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookMarkFragment : Fragment() {
    private lateinit var BookMarkAdapter: BookMarkAdapter
    private var columnCount = 1
    private var arrayList = arrayListOf<EventData>(
        EventData("건국대 학사일정", "개교기념일", 5, false),
        EventData("KBO리그", "LG vs SSG", 20, false),
        EventData("프리미어리그", "토트넘 vs 첼시", 25, false)
    )

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        BookMarkAdapter = BookMarkAdapter(arrayList)
        val view =  inflater.inflate(R.layout.fragment_book_mark_list, container, false)
        val addBookMark = view.findViewById<ImageView>(R.id.addBookMark)
        val eraseBookMark = R.layout.fragment_book_mark
        val recyclerList = view.findViewById<RecyclerView>(R.id.recyclerList)
        recyclerList.adapter = BookMarkAdapter
        val size = arrayList.size

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerList)
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                setHasFixedSize(true)
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = BookMarkAdapter(arrayList)
            }
        }

        addBookMark.setOnClickListener {
            for (i in 0 until size){
                val category = arrayList[i].category
                val event = arrayList[i].event
                val dday = arrayList[i].Dday
                arrayList[i] = EventData(category, event, dday, true)
            }
            BookMarkAdapter.setData(arrayList)
        }
        return view
    }
}