package com.example.amadda

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.FragmentBottomSheetListDialogBinding
import com.example.amadda.databinding.FragmentBottomSheetListDialogItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


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
class BottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetListDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetListDialogBinding.inflate(inflater, container, false)
        binding.addCategory.setOnClickListener{
            val intent = Intent(context, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        val bottomSheetFragment = BottomSheet()
        binding.todoAddBtn.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().remove(this@BottomSheet).commit()
            fragmentManager.popBackStack()

        }

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val categories = listOf(
            Category(0,"약속","."),
            Category(0,"알바","."),
            Category(0,"공부",".")
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