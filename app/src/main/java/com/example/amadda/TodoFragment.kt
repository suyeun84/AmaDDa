package com.example.amadda

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amadda.databinding.FragmentTodoBinding
import com.example.amadda.databinding.TodoRowBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

var bookmarkNum = 0

class TodoFragment : DialogFragment() {
    private lateinit var binding: FragmentTodoBinding
    private lateinit var bindingRow: TodoRowBinding
    lateinit var rdb: DatabaseReference
    lateinit var adapter_todo: TodoRecyclerAdapter
    lateinit var mydata: MyData
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

        val bundle = arguments
        @Suppress("DEPRECATION")
        userId = bundle?.getString("userId").toString()
        Log.d("todoList", userId)
        val notice = bundle?.getSerializable("data")
        mydata = notice as MyData
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        bindingRow = TodoRowBinding.inflate(layoutInflater)
//        userId = arguments?.getString("userId").toString()
        binding.todoFrag.setBackgroundColor(Color.TRANSPARENT)

        adapter_todo = TodoRecyclerAdapter(mydata.event)
        binding.recyclerViewTodo.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTodo.adapter = adapter_todo

        adapter_todo.itemClickListener = object : TodoRecyclerAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun OnClick(
                data: EventData,
                holder: TodoRecyclerAdapter.ViewHolder,
                position: Int
            ) {
                rdb = Firebase.database.getReference("Users/user/" + userId)
                val event = data
                Log.d("todolist", "bookmarkNum = " + bookmarkNum)
                event.star = !event.star
//                rdb.child(event.event).child("star").setValue(event.star)
                adapter_todo.notifyDataSetChanged()
                if (event.star) {
                    //firebase에 넣기
                    event.countNum = bookmarkNum
                    rdb.child("bookmarkList/"+bookmarkNum.toString()).setValue(event)
                    Log.d("todoList", "bookmark number:"+bookmarkNum.toString())
                    bookmarkNum++
                } else {
                    //firebase에서 remove
                    rdb.child("bookmarkList").get().addOnSuccessListener { dataSnapshot ->
                        GlobalScope.launch(Dispatchers.Main) {
                            if (dataSnapshot.exists()) {
                                val listType =
                                    object : GenericTypeIndicator<ArrayList<EventData>>() {}
                                val subArr = dataSnapshot.getValue(listType)
                                Log.d("todoList", "subArr = "+subArr.toString())
                                if (subArr != null) {
                                    for (i in subArr.indices) {
                                        if(subArr[i] == null)
                                            continue
                                        Log.d("todoList", "i = "+i.toString())
                                        Log.d("todoList", "subArr[$i] = "+subArr[i].toString())
                                        val getevent = EventData(
                                            subArr[i].category.toString(),
                                            subArr[i].event.toString(),
                                            subArr[i].tag.toString(),
                                            subArr[i].dDay.toInt(),
                                            subArr[i].edit,
                                            subArr[i].star,
                                            subArr[i].countNum
                                        )
                                        Log.d("todoList", "getevent = "+getevent.toString())
                                        Log.d("todoList", "dataevent = "+data.event.toString())

                                        if (getevent.event == data.event) {
                                            val parentKey = getevent.countNum
                                            Log.d("todoList", "parentKey = "+parentKey.toString())
                                            rdb.child("bookmarkList/"+parentKey.toString()).removeValue()
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                adapter_todo.notifyDataSetChanged()
            }
        }

        adapter_todo.detailClickListener = object: TodoRecyclerAdapter.OnItemClickListener{
            override fun OnClick(
                data: EventData,
                holder: TodoRecyclerAdapter.ViewHolder,
                position: Int
            ) {
                val intent = Intent(requireContext(), TodoDetailActivity::class.java)
                intent.putExtra("data", data)
                intent.putExtra("year", mydata.date.substring(0,4))
                intent.putExtra("month", mydata.date.substring(4,6))
                intent.putExtra("day", mydata.date.substring(6,8))
                startActivity(intent)
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetFragment = BottomSheet()
        binding.buttonAdd.setOnClickListener {
            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag())
        }

        val now = LocalDate.now()
        val dDay = LocalDate.of(
            mydata.date.substring(0, 4).toInt(),
            mydata.date.substring(4, 6).toInt(),
            mydata.date.substring(6, 8).toInt()
        ) // D-Day를 설정합니다.
        val daysDiff = ChronoUnit.DAYS.between(now, dDay)
        binding.textViewDay.text =
            mydata.date.substring(4, 6) + "월 " + mydata.date.substring(6, 8) + "일"

        if (daysDiff > 0) {
            binding.textViewDday.text = "D - " + daysDiff.toString()
        } else if (daysDiff < 0) {
            binding.textViewDday.text = "D + " + (daysDiff * -1).toString()
        } else {
            binding.textViewDday.text = "D - DAY"
        }


    }
}
