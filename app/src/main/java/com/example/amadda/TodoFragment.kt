package com.example.amadda

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

var bookmarkNum = 0

class TodoFragment : DialogFragment(), DataListener {
    private lateinit var binding: FragmentTodoBinding
    private lateinit var bindingRow: TodoRowBinding
    lateinit var rdb: DatabaseReference
    lateinit var adapter_todo: TodoRecyclerAdapter
    lateinit var mydata: MyData
    var mytodo: EventData = EventData()
    lateinit var userId: String
    lateinit var year: String

    lateinit var colorArr: ArrayList<Category>

//    private fun openBottomSheet() {
//        val bottomSheet = BottomSheet()
//        bottomSheet.setDataListener(this)
//        bottomSheet.show(parentFragmentManager, "bottomSheetTag")
//    }

    // 데이터 수신 콜백 메서드 구현
    override fun onDataReceived(data: EventData) {
        // 데이터 처리 로직
        Log.d("clickablee", "received : $data")
        mytodo = data
//        val view = requireView().findViewById<View>(R.id.rowTodo)
//        val background = view.background

        if(mytodo != null && mytodo.event.isNotEmpty()){
            var todos: ArrayList<EventData> = ArrayList<EventData>()
            rdb.child("todoList").get().addOnSuccessListener { dataSnapshot ->
                GlobalScope.launch(Dispatchers.Main) {
                    if(dataSnapshot.exists()){
                        val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                        val todoArr = dataSnapshot.getValue(listType)
                        if (todoArr != null) {
//                            for (i in todoArr.indices) {
//                                if (todoArr[i] != null) {
//                                    todos.add(todoArr[i])
//                                }
//                            }
                            val date = binding.textViewDay.text.filter { it.isDigit() }
//                            binding.textViewDay
                            mytodo.date = year + date
                            todoArr.add(mytodo)

                            rdb.child("todoList").setValue(todoArr)
                                .addOnSuccessListener {
                                    // 데이터 추가 성공 시 실행되는 코드
                                    Log.d("Firebase", "Todo added successfully")

                                    EventBus.getDefault().post(BusEvent(1))
                                }
                                .addOnFailureListener { exception ->
                                    // 데이터 추가 실패 시 실행되는 코드
                                    Log.e("Firebase", "Failed to add todo: ${exception.message}")
                                }
                            Log.d("subArr", "here $todoArr")
                        }
                    } else {
                        val todoArr = ArrayList<EventData>()
                        val date = binding.textViewDay.text.filter { it.isDigit() }
//                            binding.textViewDay
                        mytodo.date = year + date
                        todoArr.add(mytodo)

                        rdb.child("todoList").setValue(todoArr)
                            .addOnSuccessListener {
                                // 데이터 추가 성공 시 실행되는 코드
                                Log.d("Firebase", "Todo added successfully")
                                EventBus.getDefault().post(BusEvent(1))
                            }
                            .addOnFailureListener { exception ->
                                // 데이터 추가 실패 시 실행되는 코드
                                Log.e("Firebase", "Failed to add todo: ${exception.message}")
                            }
                    }

                    // mytodo를 calendar에 뿌려줘야 돼

                }

            }
            adapter_todo.notifyDataSetChanged()
        }



        dismiss()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

        val bundle = arguments
        @Suppress("DEPRECATION")
        userId = bundle?.getString("userId").toString()
        year = bundle?.getString("year").toString()
//        colorArr = bundle?.getSerializable("category") as ArrayList<Category>

        Log.d("todoList", userId)
        val notice = bundle?.getSerializable("data")
        mydata = notice as MyData
    }

    private fun saveLike(subArr: ArrayList<EventData>, completion: (Boolean) -> Unit) {
        rdb.child("bookmarkList").setValue(subArr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(true)
                } else {
                    completion(false)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        bindingRow = TodoRowBinding.inflate(layoutInflater)
//        userId = arguments?.getString("userId").toString()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        rdb = Firebase.database.getReference("Users/user/" + userId)
        var userLikes: ArrayList<EventData> = ArrayList<EventData>()

        rdb.child("bookmarkList").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                if (dataSnapshot.exists()) {
                    val listType =
                        object : GenericTypeIndicator<ArrayList<EventData>>() {}
                    val subArr = dataSnapshot.getValue(listType)
                    if (subArr != null) {
                        for (i in subArr.indices) {
                            if (subArr[i] != null) {
                                userLikes.add(subArr[i])
                            }
                        }
                    }
                }



                rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
                    GlobalScope.launch(Dispatchers.Main) {
                        if (dataSnapshot.exists()) {
                            val listType =
                                object : GenericTypeIndicator<ArrayList<Category>>() {}
                            val subArr1 = dataSnapshot.getValue(listType)
                            if (subArr1 != null) {
                                Log.d("categoryArr", "$subArr1")
                                colorArr = subArr1
                                adapter_todo = TodoRecyclerAdapter(mydata.event, userLikes)
                                adapter_todo.categoryArr = subArr1
                                binding.recyclerViewTodo.layoutManager = LinearLayoutManager(requireContext())
                                binding.recyclerViewTodo.adapter = adapter_todo
                                adapter_todo.notifyDataSetChanged()

                                adapter_todo.itemClickListener = object : TodoRecyclerAdapter.OnItemClickListener {
                                    @SuppressLint("NotifyDataSetChanged")
                                    override fun OnClick(
                                        data: EventData,
                                        holder: TodoRecyclerAdapter.ViewHolder,
                                        position: Int
                                    ) {
                                        rdb = Firebase.database.getReference("Users/user/" + userId)
                                        lateinit var event: EventData
                                        rdb.child("bookmarkList").get().addOnSuccessListener { dataSnapshot ->
                                            if (dataSnapshot.exists()) {

                                                val listType =
                                                    object : GenericTypeIndicator<ArrayList<EventData>>() {}
                                                val subArr = dataSnapshot.getValue(listType)

                                                var found: Boolean = false
                                                if (subArr != null) {
                                                    for (i in subArr.indices) {
                                                        if (subArr[i] != null) {
                                                            if (subArr[i].category == data.category && subArr[i].code == data.code) {
                                                                event = subArr[i]
                                                                event.star = false
                                                                // tag
                                                                found = true

                                                                subArr.remove(subArr[i])
                                                                saveLike(subArr) {

                                                                }
                                                                adapter_todo.likes = subArr
                                                                adapter_todo.notifyItemChanged(position)
                                                                adapter_todo.notifyDataSetChanged()
                                                                break
                                                            }
                                                        }
                                                    }
                                                    if (!found) {
//                                        if ((data.category == "konkuk" || data.category == "KBO"
//                                                    || data.category == "Premier" || data.category == "festival")) {
//
//                                        }
                                                        event = data
                                                        event.star = true
                                                        subArr.add(event)
                                                        saveLike(subArr) {

                                                        }
                                                        adapter_todo.likes = subArr
                                                        adapter_todo.notifyItemChanged(position)
                                                        adapter_todo.notifyDataSetChanged()
                                                    }
                                                }}
                                            else {
                                                val subArr = ArrayList<EventData>()
                                                event = data
                                                event.star = true
                                                subArr.add(event)
                                                saveLike(subArr) {

                                                }
                                                adapter_todo.likes = subArr
                                                adapter_todo.notifyItemChanged(position)
                                                adapter_todo.notifyDataSetChanged()
//                                if ((data.category == "konkuk" || data.category == "KBO"
//                                            || data.category == "Premier" || data.category == "festival")) {
//
//                                }

                                            }
                                        }
//                        val event = data
                                        Log.d("todolist", "bookmarkNum = " + bookmarkNum)

                                        adapter_todo.notifyDataSetChanged()
                                        adapter_todo.notifyItemChanged(position)
                                        adapter_todo.notifyDataSetChanged()
                                    }
                                }

                                adapter_todo.checkClickListener = object : TodoRecyclerAdapter.OnItemClickListener{
                                    override fun OnClick(
                                        data: EventData,
                                        holder: TodoRecyclerAdapter.ViewHolder,
                                        position: Int
                                    ) {
                                        Log.d("subARRRRR", "here")
                                        rdb = Firebase.database.getReference("Users/user/" + userId)
                                        rdb.child("todoList").get().addOnSuccessListener { dataSnapshot ->
                                            if (dataSnapshot.exists()) {
                                                Log.d("subARRRRR", "here2")
                                                val listType =
                                                    object :
                                                        GenericTypeIndicator<ArrayList<EventData>>() {}
                                                val subArr3 = dataSnapshot.getValue(listType)

                                                if (subArr3 != null) {
                                                    Log.d("subARRRRR", "here3")
                                                    for (i in subArr3.indices) {
                                                        Log.d("subARRRRR", "here4")
                                                        if (subArr3[i] != null) {
                                                            Log.d("subARRRRR", "here5")
                                                            if (subArr3[i].category == data.category
                                                                && subArr3[i].event == data.event
                                                                && subArr3[i].date == data.date
                                                            ) {
                                                                Log.d("subARRRRR", "${subArr3[i]}")
                                                                if (subArr3[i].star) {
                                                                    adapter_todo.items[position].star = false
                                                                    subArr3[i].star = false
                                                                    adapter_todo.notifyItemChanged(position)

                                                                } else {
                                                                    adapter_todo.items[position].star = true
                                                                    subArr3[i].star = true
                                                                    adapter_todo.notifyItemChanged(position)
                                                                }
                                                                rdb.child("todoList").setValue(subArr3)
                                                                    .addOnCompleteListener { task ->
                                                                    }
                                                                break
                                                            }


                                                        }
                                                    }
                                                }
                                            }
                                        }

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
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetFragment = BottomSheet()
        bottomSheetFragment.setDataListener(this)
        bottomSheetFragment.userId = userId
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

    fun addTodoToList(todo: EventData) {
        if(todo != null){
            var todos: ArrayList<EventData> = ArrayList<EventData>()
            rdb.child("todoList").get().addOnSuccessListener { dataSnapshot ->
                GlobalScope.launch(Dispatchers.Main) {
                    if(dataSnapshot.exists()){
                        val listType = object : GenericTypeIndicator<ArrayList<EventData>>() {}
                        val todoArr = dataSnapshot.getValue(listType)
                        if (todoArr != null) {
                            for (i in todoArr.indices) {
                                if (todoArr[i] != null) {
                                    todos.add(todoArr[i])
                                }
                            }
                        }
                    }

                }
            }
            todos.add(todo)
            rdb.child("todoList").setValue(todos)
                .addOnSuccessListener {
                    // 데이터 추가 성공 시 실행되는 코드
                    Log.d("Firebase", "Todo added successfully")
                }
                .addOnFailureListener { exception ->
                    // 데이터 추가 실패 시 실행되는 코드
                    Log.e("Firebase", "Failed to add todo: ${exception.message}")
                }
            var totalArr = ArrayList<EventData>()

            for(i in mydata.event.indices){
                totalArr.add(mydata.event[i])
            }
            for (i in todos.indices){
                totalArr.add(todos[i])
            }
        }
        // 데이터가 추가되었으므로 RecyclerView 등을 업데이트하는 로직을 구현
        // adapter_todo.notifyDataSetChanged() 등으로 RecyclerView 갱신
    }

    override fun onStart() {
        super.onStart()
        Log.d("subArr", "todo - onStart")
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        Log.d("subArr", "todo - onStop")
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun printId(event: ToDoEvent) {
        Log.d("subArr", "before print ${adapter_todo.likes}")

//        adapter_todo.items = event.data

        adapter_todo.itemRenew(event.data)
//        adapter_todo.likes =
        Log.d("subArr", "middle print ${adapter_todo.likes}")
        adapter_todo.notifyDataSetChanged()
        Log.d("subArr", "after print ${adapter_todo.likes}")
//        Log.d("subArr", "")
//        if (event.data == 1) {
//            Log.d("subArr", "received")
//            Toast.makeText(context, "${event.id}", Toast.LENGTH_SHORT).show()
//            updateCalendar(subscribeArr)
//        }
    }


}
