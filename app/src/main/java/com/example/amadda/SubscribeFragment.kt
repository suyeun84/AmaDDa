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
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SubscribeFragment : Fragment() {

    lateinit var binding: FragmentSubscribeBinding
    lateinit var mbinding: ActivityMainBinding
    val data:ArrayList<Subscription> = ArrayList()

    lateinit var rdb: DatabaseReference
    lateinit var adapter: SubscribeAdapter
    var userId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("userId").toString()

        data.add(Subscription("건국대", R.drawable.subscribe_logo_1, false))

        binding = FragmentSubscribeBinding.inflate(inflater, container, false)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                data.clear()
                Log.d("시발","...")
                when (tab.position) {
                    0 -> {
                        data.add(Subscription("건국대", R.drawable.subscribe_logo_1, false))
                    }
                    1 -> {
                        data.add(Subscription("아스널 FC", R.drawable.arsenal, false))
                        data.add(Subscription("아스톤 빌라 FC", R.drawable.astonvila, false))
                        data.add(Subscription("첼시 FC", R.drawable.chelsea, false))
                        data.add(Subscription("에버턴 FC", R.drawable.everton, false))
                        data.add(Subscription("풀럼 FC", R.drawable.fulham, false))
                        data.add(Subscription("리버풀 FC", R.drawable.liverpool, false))
                        data.add(Subscription("맨체스터 시티 FC", R.drawable.mancity, false))
                        data.add(Subscription("맨체스터 유나이티드 FC", R.drawable.manutd, false))
                        data.add(Subscription("뉴캐슬 유나이티드 FC", R.drawable.newcastle, false))
                        data.add(Subscription("토트넘 훗스퍼 FC", R.drawable.tottenham, false))
                        data.add(Subscription("웨스트햄 유나이티드 FC", R.drawable.westham, false))
                    }
                    2 -> {
                        data.add(Subscription("SSG", R.drawable.ssg, false))
                        data.add(Subscription("LG", R.drawable.lgtwins, false))
                        data.add(Subscription("NC", R.drawable.nc, false))
                        data.add(Subscription("롯데", R.drawable.lotte, false))
                        data.add(Subscription("두산", R.drawable.doosan, false))
                        data.add(Subscription("KIA", R.drawable.kia, false))
                        data.add(Subscription("키움", R.drawable.kiwoom, false))
                        data.add(Subscription("KT", R.drawable.ktwiz, false))
                        data.add(Subscription("삼성", R.drawable.lions, false))
                        data.add(Subscription("환화", R.drawable.eaglew, false))
                    }
                    3->{


                        data.add(Subscription("페스티벌", R.drawable.subscribe_logo_4, false))
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Do nothing
            }
        })
        initRecyclerView()
        mbinding = ActivityMainBinding.inflate(inflater, container, false)

        rdb = Firebase.database.getReference("Users/user/" + userId)
         rdb.child("subscribe").get().addOnSuccessListener { dataSnapshot ->
            GlobalScope.launch(Dispatchers.Main) {
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    val listType = object : GenericTypeIndicator<ArrayList<Int>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (i in subArr.indices) {
                            val index = subArr[i]
                            if (index != null && index < data.size) {
                                data[index].isSubscribing = true
                            }
                        }

                        rdb.child("subscribe").setValue(subArr)
                    }
                }
                initRecyclerView() // 비동기 작업이 완료된 후에 initRecyclerView()를 호출
            }
        }
        return binding.root
    }
    
    private fun initRecyclerView() {
        binding.apply {
            subscribeRecyclerView.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
            adapter = SubscribeAdapter(data)
            adapter.itemClickListener = object : SubscribeAdapter.OnItemClickListener {
                override fun OnItemClick(data: Subscription, position: Int) {
                    rdb = Firebase.database.getReference("Users/user/" + userId)
                    if (data.isSubscribing) {
                        data.isSubscribing = false
                        rdb.child("subscribe").get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val listType = object : GenericTypeIndicator<ArrayList<Int>>() {}
                                val subArr = dataSnapshot.getValue(listType)

                                if (subArr != null) {
                                    subArr.remove(position)
                                    saveSubscription(subArr) { success ->
                                        if (success) {
                                            // 데이터베이스에 변경된 값 저장 후 처리할 작업 수행
                                        } else {
                                            // 저장 실패 처리
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        data.isSubscribing = true
                        rdb.child("subscribe").get().addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                val listType = object : GenericTypeIndicator<ArrayList<Int>>() {}
                                val subArr = dataSnapshot.getValue(listType)
                                if (subArr != null) {
                                    if (!subArr.contains(position)) {
                                        subArr.add(position)
                                        saveSubscription(subArr) { success ->
                                            if (success) {
                                                // 데이터베이스에 변경된 값 저장 후 처리할 작업 수행
                                            } else {
                                                // 저장 실패 처리
                                            }
                                        }
                                    }
                                }
                            } else {
                                val subArr = ArrayList<Int>()
                                subArr.add(position)
                                saveSubscription(subArr) {success ->
                                    if (success) {
                                        // 데이터베이스에 변경된 값 저장 후 처리할 작업 수행
                                        Log.d("saveSubscription", "canceled success")
                                    } else {
                                        // 저장 실패 처리
                                        Log.d("saveSubscription", "canceled failed")
                                    }
                                }
                            }
                        }
                    }

                    adapter.notifyItemChanged(position)
                }
            }
            subscribeRecyclerView.adapter = adapter
        }
    }

    private fun saveSubscription(subArr: ArrayList<Int>, completion: (Boolean) -> Unit) {
        rdb.child("subscribe").setValue(subArr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(true)
                } else {
                    completion(false)
                }
            }
    }
}