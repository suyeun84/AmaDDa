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
import androidx.fragment.app.FragmentManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.FragmentBottomSheetListDialogBinding
import com.example.amadda.databinding.FragmentBottomSheetListDialogItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// TODO: Customize parameter argument names

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    BottomSheet.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class BottomSheet() : BottomSheetDialogFragment() {

    var userId: String = ""
    lateinit var rdb: DatabaseReference
    lateinit var adapter: CategoryAdapter

    private var _binding: FragmentBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var categoryArr: ArrayList<Category> = ArrayList()

    private var dataListener: DataListener? = null

    // 데이터 리스너 설정 메서드
    fun setDataListener(listener: DataListener) {
        dataListener = listener
    }

    // 데이터 전달 메서드
    private fun sendData(data: EventData) {
        dataListener?.onDataReceived(data)
    }

    override fun onResume() {
        super.onResume()
        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                categoryArr.clear()

                val listType = object : GenericTypeIndicator<ArrayList<Category>>() {}
                val subArr = dataSnapshot.getValue(listType)

                if (subArr != null) {
                    for (i in subArr.indices) {
                        if (subArr[i] != null) {
                            categoryArr.add(subArr[i])
                        }
                    }
                }
                rdb.child("todoCategory").setValue(subArr)
                    .addOnCompleteListener { task ->
                    }
                adapter.notifyDataSetChanged()

            } else {
                val subArr = ArrayList<Category>()
                subArr.add(Category("약속", "#FFB800"))
                subArr.add(Category("알바","#4C4C67"))
                subArr.add(Category("공부","#AECFFF"))
                rdb.child("todoCategory").setValue(subArr)
                    .addOnCompleteListener { task ->
                    }
                adapter.notifyDataSetChanged()
            }

        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetListDialogBinding.inflate(inflater, container, false)
        binding.addCategory.setOnClickListener{
            val intent = Intent(context, AddCategoryActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val bottomSheetFragment = BottomSheet()
//        binding.todoAddBtn.setOnClickListener {
//            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            fragmentManager.beginTransaction().remove(this@BottomSheet).commit()
//            fragmentManager.popBackStack()
//
//        }

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                categoryArr.clear()

                val listType = object : GenericTypeIndicator<ArrayList<Category>>() {}
                val subArr = dataSnapshot.getValue(listType)

                if (subArr != null) {
                    for (i in subArr.indices) {
                        if (subArr[i] != null) {
                            categoryArr.add(subArr[i])
                        }
                    }
                }
                rdb.child("todoCategory").setValue(subArr)
                    .addOnCompleteListener { task ->
                    }
                adapter = CategoryAdapter(categoryArr)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener { category ->
                    handleCategoryItemClick(category)
                }
            } else {
                val subArr = ArrayList<Category>()
                subArr.add(Category("약속", "#FFB800"))
                subArr.add(Category("알바","#4C4C67"))
                subArr.add(Category("공부","#AECFFF"))
                rdb.child("todoCategory").setValue(subArr)
                    .addOnCompleteListener { task ->
                    }
                adapter = CategoryAdapter(subArr)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener { category ->
                    handleCategoryItemClick(category)
                }
            }

        }
//        val categories = listOf(
//            Category("약속","#C88888"),
//            Category("알바","#C7D695"),
//            Category("공부","#C082E9")
//        )


    }

    private fun handleCategoryItemClick(category: Category) {

        binding.input.requestFocus()
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        binding.categorySelect.visibility = View.GONE
        binding.todoInput.visibility = View.VISIBLE

        rdb = Firebase.database.getReference("Users/user/" + userId)
        binding.todoAddBtn.setOnClickListener {

            if (binding.input.text != null) {

                val inputTodo = binding.input.text.toString()
                val todo = EventData(
                    category.title,
                    inputTodo,
                    0,
                    false,
                    false,
                    0,
                    "202030615"
                )
                Log.d("subArr", "sent! : ${todo}")

                val bundle = Bundle()
                bundle.putSerializable("inputTodo", todo)
                bundle.putSerializable("category", categoryArr)
                Log.d("inputodo", todo.toString())

                val fragment = TodoFragment()
                fragment.arguments = bundle
                sendData(todo)


                binding.input.text.clear()

                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                fragmentManager.beginTransaction().remove(this@BottomSheet).commit()
                fragmentManager.popBackStack()
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