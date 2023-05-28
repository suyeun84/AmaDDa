package com.example.amadda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookMarkFragment : Fragment() {

    private var columnCount = 1
    private var arrayList = arrayListOf<EventData>(
        EventData("건국대 학사일정", "개교기념일", 5),
        EventData("KBO리그", "LG vs SSG", 20),
        EventData("프리미어리그", "토트넘 vs 첼시", 25)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_book_mark_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = BookMarkAdapter(arrayList)
            }
        }
        return view
    }
}