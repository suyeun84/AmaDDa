package com.example.amadda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amadda.databinding.ActivityMainBinding
import com.example.amadda.databinding.FragmentSubscribeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SubscribeFragment : Fragment() {

    lateinit var binding: FragmentSubscribeBinding
    lateinit var mbinding: ActivityMainBinding
    val data:ArrayList<Subscription> = ArrayList()

    lateinit var rdb: DatabaseReference
    lateinit var adapter: SubscribeAdapter
    val userId: String = "blueme0"
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
        mbinding = ActivityMainBinding.inflate(inflater, container, false)
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
                    if (data.isSubscribing) {
                        rdb = Firebase.database.getReference("Users/user/" + userId)
                        rdb.child("subscribe").get().addOnSuccessListener {
//                            it.value.toString()
                            var subArr: ArrayList<Int> = it.value as ArrayList<Int>
                            Log.d("initRecyclerViewrdb", "it value class : ${(it.value)!!::class}")
                            Log.d("initRecyclerViewrdb", "subArr class : ${ subArr::class}")
//                            subArr.remove(subArr.indexOf(position))
                            Log.d("initRecyclerViewrdb", "it value : ${it.value}")
                            Log.d("initRecyclerViewrdb", "position is ... $position")
                            Log.d("initRecyclerViewrdb", subArr.toString())
                            Log.d("initRecyclerViewrdb", subArr.indexOf(0).toString())
                            rdb.child("/subscribe").setValue(subArr)
//                            Log.d("initRecyclerViewrdb", subArr.toString())
                        }
//                        Log.d("initRecyclerViewrdb", str.toString())



//                        rdb = Firebase.database.getReference("Users/user/" + binding.editTextId.text.toString() + "/categoryList")
//                        userinfo.category = cateArr
//                        rdb.child("category").setValue(userinfo.category)
                    }
                    else {

                    }
                    adapter.notifyItemChanged(position)
                }
            }
            subscribeRecyclerView.adapter = adapter
        }
    }

}