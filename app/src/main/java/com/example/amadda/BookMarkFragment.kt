package com.example.amadda

import android.annotation.SuppressLint
import android.media.metrics.Event
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BookMarkFragment : Fragment() {
    private lateinit var BookMarkAdapter: BookMarkAdapter
    private var columnCount = 1
    lateinit var rdb: DatabaseReference
    var count: Int = 0
    private var arrayList = arrayListOf<EventData>(
//        EventData(0,"건국대 학사일정", "개교기념일", 5, false),
//        EventData(1,"KBO리그", "LG vs SSG", 20, false),
//        EventData(2,"프리미어리그", "토트넘 vs 첼시", 25, false)
    )

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rdb = Firebase.database.getReference("Users/user/kelsey6225/bookmarkList")
        rdb.child("0").get().addOnSuccessListener {
            var bookmark = it.value as ArrayList<EventData>
            Log.i("size", bookmark.toString())
        }
        BookMarkAdapter = BookMarkAdapter(arrayList)
        val view = inflater.inflate(R.layout.fragment_book_mark_list, container, false)
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

//                rdb.child(binding.editTextId.text.toString()).setValue(userinfo)
//                rdb = Firebase.database.getReference("Users/user/kelsey6225")
//                rdb.child("bookmarkList").get().addOnSuccessListener {
//                    var bookmark = it.value as ArrayList<EventData>
//                    Log.i("size", bookmark.size.toString())
//                }

                adapter = BookMarkAdapter(arrayList)
            }
        }

//        addBookMark.setOnClickListener {
//            for (i in 0 until size) {
//                val category = arrayList[i].category
//                val event = arrayList[i].event
//                val dday = arrayList[i].Dday
//                arrayList[i] = EventData(category, event, dday, true)
//            }
//            BookMarkAdapter.setData(arrayList)
//        }
        return view
    }

//    private fun getBookmark() {
//        rdb = Firebase.database.getReference("Users/user/aaaaaaa1/bookmark")
//        rdb.child("eventid").get().addOnSuccessListener {
//            if (it.value != null) {
//                rdb = Firebase.database.getReference("Events/konkuk")
//                rdb.child(it.value.toString()).get().addOnSuccessListener {
//                    val eventinfo = EventData(
//                        "konkuk",
//                        "모프",
//                        0,
//                        false
//                    )
//                }
//            }
//        }
//    }
}