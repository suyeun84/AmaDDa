package com.example.amadda

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amadda.databinding.ActivityAddCategoryBinding


class AddCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    private lateinit var categoryList: MutableList<Category>
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout();
        initBtn();

    }

    fun initLayout() {
        var imageView8 = binding.imageView8
        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
        binding.button.setOnClickListener {
         //   val categoryText = binding.editTextTextCategory.text.toString()
            // 가져온 카테고리 데이터를 리스트에 추가
         //   categoryList.add(Category(0,categoryText,"."),)
            // 어댑터에 데이터 변경 알림
        //    categoryAdapter.notifyDataSetChanged()
            finish()
        }
    }
    fun initBtn(){
        val imageViews = arrayOf(
            binding.imageView4,
            binding.imageView5,
            binding.imageView6,
            binding.imageView7,
            binding.imageView8
        )

        val selectedResources = arrayOf(
            R.drawable.circle4select,
            R.drawable.circle5select,
            R.drawable.circle6select,
            R.drawable.circle7select,
            R.drawable.circle8select
        )

        val unselectedResources = arrayOf(
            R.drawable.circle4,
            R.drawable.circle5,
            R.drawable.circle6,
            R.drawable.circle7,
            R.drawable.circle8
        )

        for (i in imageViews.indices) {
            imageViews[i].setOnClickListener {
                for (j in imageViews.indices) {
                    imageViews[j].setImageResource(if (i == j) selectedResources[j] else unselectedResources[j])
                }
            }
        }

    }

}