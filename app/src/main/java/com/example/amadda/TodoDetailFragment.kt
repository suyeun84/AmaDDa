package com.example.amadda

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amadda.databinding.ActivityDetaliBinding
import com.example.amadda.databinding.ActivityTodoDetailBinding
import com.example.amadda.databinding.FragmentCalendarBinding
import com.example.amadda.databinding.FragmentSettingBinding

class TodoDetailFragment : Fragment() {
    lateinit var binding: ActivityTodoDetailBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoDetailBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityTodoDetailBinding.inflate(inflater, container, false)
        return binding.root

        binding.shareBtnAppbar.setOnClickListener {
            val data = binding.textViewTitle.text.toString()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, data)
            }
            startActivity(Intent.createChooser(intent, data))
        }


        // Inflate the layout for this fragment
         }

}