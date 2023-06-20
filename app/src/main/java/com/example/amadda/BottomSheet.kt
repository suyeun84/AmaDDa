package com.example.amadda

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.FragmentBottomSheetListDialogBinding
import com.example.amadda.databinding.FragmentBottomSheetListDialogItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class BottomSheet : BottomSheetDialogFragment() {
    lateinit var rdb: DatabaseReference
    private var _binding: FragmentBottomSheetListDialogBinding? = null
//    private lateinit var listener: MyDataListener

//    interface MyDataListener {
//        fun onDataReceived(data: Todo)
//    }
//
//    fun setMyDataListener(listener: MyDataListener) {
//        this.listener = listener
//    }
//
//    // 데이터 전달 로직을 처리하는 함수
//    private fun sendDataToFragment(data: Todo) {
//        listener.onDataReceived(data)
//        dismiss()
//    }
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetListDialogBinding.inflate(inflater, container, false)
        binding.addCategory.setOnClickListener {
            val intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val categories = listOf(
            Category(0, "약속", "."),
            Category(0, "알바", "."),
            Category(0, "공부", ".")
        )

        val adapter = CategoryAdapter(categories)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener { category ->
            handleCategoryItemClick(category)
        }
    }


    private fun handleCategoryItemClick(category: Category) {

        binding.input.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        binding.categorySelect.visibility = View.GONE
        binding.todoInput.visibility = View.VISIBLE

        rdb = Firebase.database.getReference("Users/user/kelsey6225")
        binding.todoAddBtn.setOnClickListener {
            rdb.child("todoList").get().addOnSuccessListener { dataSnapshot ->
                if(dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Todo>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (binding.input.text != null) {
                        val inputTodo = binding.input.text.toString()
                        val todo = EventData(
                            "약속",
                            inputTodo,
                            0,
                            false,
                            false,
                            0,
                            "202030615"
                        )

//                        val bundle = Bundle()
//                        bundle.putSerializable("inputTodo", todo)
//                        Log.d("inputodo", todo.toString())
//
                        val fragment = TodoFragment()
//                        fragment.arguments = bundle
                        fragment.addTodoToList(todo)

                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.beginTransaction().remove(this@BottomSheet).commit()
                        fragmentManager.popBackStack()
                    }
                }
            }
        }

    }

    private inner class ViewHolder internal constructor(binding: FragmentBottomSheetListDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal val text: TextView = binding.text
    }

    private inner class ItemAdapter internal constructor(private val mItemCount: Int) :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentBottomSheetListDialogItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return mItemCount
        }
    }

    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            val inputManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onDestroyView() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.input.getWindowToken(), 0);
        super.onDestroyView()
        _binding = null
    }
}