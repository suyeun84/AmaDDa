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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookMarkFragment : Fragment() {
    lateinit var rdb: DatabaseReference
    private lateinit var BookMarkAdapter: BookMarkAdapter
    private var columnCount = 1
    private var arrayList = arrayListOf<EventData>(
        EventData("건국대 학사일정", "개교기념일", 5, false),
        EventData("KBO리그", "LG vs SSG", 20, false),
        EventData("프리미어리그", "토트넘 vs 첼시", 25, false)
    )

    var userId: String = ""

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userId = arguments?.getString("userId").toString()
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

        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("bookmarkList").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (i in subArr.indices) {
                            Log.d("bookmarkList", subArr[i].toString())
                            }
                        }

                        rdb.child("bookmarkList").setValue(subArr)
                    }
                }
            }
        return view
    }
}