package com.example.amadda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private lateinit var bookmarkAdapter: BookMarkAdapter
    private var columnCount = 1
    private var arrayList = arrayListOf<EventData>()

    val userId: String = "kelsey6225"

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_book_mark_list, container, false)

        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("bookmarkList").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (i in subArr.indices) {
//                            Log.d("bookmarkList", subArr[i].toString())
                            val event = EventData(subArr[i].category.toString(), subArr[i].event.toString(), subArr[i].Dday.toInt())
                            arrayList.add(event)
                        }
                    }
                    bookmarkAdapter = BookMarkAdapter(arrayList)
                    val recyclerList = view.findViewById<RecyclerView>(R.id.recyclerList)
                    recyclerList.adapter = bookmarkAdapter

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
                }
                val addBookMark = view.findViewById<ImageView>(R.id.addBookMark)
                val size = arrayList.size
                Log.d("bookmarkList", size.toString())

                addBookMark.setOnClickListener {
                    for (i in 0 until size){
                        val category = arrayList[i].category.toString()
                        val event = arrayList[i].event.toString()
                        var dDay = arrayList[i].Dday.toString().toInt()
                        val edit = !arrayList[i].edit

                        val eventdata = EventData(category, event, dDay, edit)
                        arrayList[i] = eventdata
                    }

                    bookmarkAdapter = BookMarkAdapter(arrayList)
                    val recyclerList = view.findViewById<RecyclerView>(R.id.recyclerList)
                    recyclerList.adapter = bookmarkAdapter
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
                }



//                val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerList)
//                recyclerView.adapter = bookmarkAdapter
            }
        }

        bookmarkAdapter = BookMarkAdapter(arrayList)
        val recyclerList = view.findViewById<RecyclerView>(R.id.recyclerList)

        bookmarkAdapter.itemClickListener = object : BookMarkAdapter.OnItemClickListener{
            override fun onItemClick(data: EventData, position: Int) {
                Log.d("bookmarkList", "click")
                arrayList.removeAt(position)
                Log.d("bookmarkList", position.toString())
//                bookmarkAdapter.notifyItemRemoved(position)
                bookmarkAdapter.notifyDataSetChanged()
            }
        }
        recyclerList.adapter = bookmarkAdapter

        return view
    }
}