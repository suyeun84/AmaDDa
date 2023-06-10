package com.example.amadda

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amadda.databinding.FragmentTodoBinding
import com.example.amadda.databinding.TodoRowBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TodoFragment : DialogFragment() {
    private lateinit var binding: FragmentTodoBinding
    private lateinit var bindingRow: TodoRowBinding
    lateinit var adapter_todo: TodoRecyclerAdapter
    lateinit var data: MyData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

        val bundle = arguments
        @Suppress("DEPRECATION")
        val notice = bundle?.getSerializable("data")
        data = notice as MyData
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        bindingRow = TodoRowBinding.inflate(layoutInflater)

        adapter_todo = TodoRecyclerAdapter(data.event)
        binding.recyclerViewTodo.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTodo.adapter = adapter_todo


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val now = LocalDate.now()
        val dDay = LocalDate.of(
            data.date.substring(0, 4).toInt(),
            data.date.substring(4, 6).toInt(),
            data.date.substring(6, 8).toInt()
        ) // D-Day를 설정합니다.
        val daysDiff = ChronoUnit.DAYS.between(now, dDay)
        binding.textViewDay.text =
            data.date.substring(4, 6) + "월 " + data.date.substring(6, 8) + "일"

        if (daysDiff > 0) {
            binding.textViewDday.text = "D - " + daysDiff.toString()
        } else if (daysDiff < 0) {
            binding.textViewDday.text = "D + " + (daysDiff * -1).toString()
        } else {
            binding.textViewDday.text = "D - DAY"
        }


    }
}