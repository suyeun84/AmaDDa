package com.example.amadda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amadda.databinding.FragmentSubscribeBinding

class SubscribeFragment : Fragment() {

    lateinit var binding: FragmentSubscribeBinding
    val data:ArrayList<Subscription> = ArrayList()

    lateinit var adapter: SubscribeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        data.add(Subscription("건국대 학사일정", R.drawable.subscribe_logo_1, true))
        data.add(Subscription("프리미어리그", R.drawable.subscribe_logo_2, true))
        data.add(Subscription("KBO리그", R.drawable.subscribe_logo_3, false))
        data.add(Subscription("페스티벌", R.drawable.subscribe_logo_4, true))
        data.add(Subscription("장학금", R.drawable.subscribe_logo_5, false))
        data.add(Subscription("공모전", R.drawable.subscribe_logo_2, false))

        binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.apply {
            subscribeRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
            adapter = SubscribeAdapter(data)
            adapter.itemClickListener = object : SubscribeAdapter.OnItemClickListener {
                override fun OnItemClick(data: Subscription, position: Int) {
                    data.isSubscribing = !data.isSubscribing
                    adapter.notifyItemChanged(position)
                }
            }
            subscribeRecyclerView.adapter = adapter
        }
    }

}