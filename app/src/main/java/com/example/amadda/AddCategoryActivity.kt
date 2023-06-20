package com.example.amadda

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import com.example.amadda.databinding.ActivityAddCategoryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AddCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    private lateinit var categoryList: MutableList<Category>
    private lateinit var categoryAdapter: CategoryAdapter
    lateinit var rdb: DatabaseReference

    var selectedColor: Int = 0

    var categoryArr: ArrayList<Category> = ArrayList()


    var userId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getStringExtra("userId").toString()


        rdb = Firebase.database.getReference("Users/user/" + userId)
        rdb.child("todoCategory").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                categoryArr.clear()

                val listType = object : GenericTypeIndicator<ArrayList<Category>>() {}
                val subArr = dataSnapshot.getValue(listType)
                if (subArr != null)
                    categoryArr = subArr
            }
            else {
                val subArr = ArrayList<Category>()
                categoryArr = subArr
            }

            initBtn()
            initLayout()

        }
    }

    fun initLayout() {
        binding.backBtnAppbar.setOnClickListener {
            finish()
        }


    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

    fun initBtn(){

        val circleColors = arrayOf(
            R.color.circle1,
            R.color.circle2,
            R.color.circle3,
            R.color.circle4,
            R.color.circle5,
            R.color.circle6,
            R.color.circle7,
            R.color.circle8
        )

        val colorArr = arrayOf(
            "#FFB800",
            "#4C4C67",
            "#AECFFF",
            "#FF6F6F",
            "#69CE67",
            "#CB88FF",
            "#DBDBDB",
            "#FF9B3F"
        )

        binding.button.setOnClickListener {
            //   val categoryText = binding.editTextTextCategory.text.toString()
            // 가져온 카테고리 데이터를 리스트에 추가
            //   categoryList.add(Category(0,categoryText,"."),)
            // 어댑터에 데이터 변경 알림
            //    categoryAdapter.notifyDataSetChanged()
            var present: Boolean = false
            val text = binding.editTextTextCategory.text.trim().toString()
            if (text.isNotEmpty()) {
                for (i in categoryArr.indices) {
                    if (categoryArr[i].title == text) {
                        present = true
                        break
                    }
                }
                if (present) {
                    Toast.makeText(this, "이미 존재하는 카테고리입니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    categoryArr.add(Category(text, colorArr[selectedColor + 3]))
                    rdb.child("todoCategory").setValue(categoryArr)
                        .addOnCompleteListener { task ->
                        }
                    finish()
                }
            }

        }

        val imageViews = arrayOf(
            binding.imageView4,
            binding.imageView5,
            binding.imageView6,
            binding.imageView7,
            binding.imageView8
        )

        val checkedResources = arrayOf(
            R.drawable.circle4check,
            R.drawable.circle5check,
            R.drawable.circle6check,
            R.drawable.circle7check,
            R.drawable.circle8check
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
                Log.d("wherestar", "i == ${i}")
                for (j in imageViews.indices) {
                    if (imageViews[j].isClickable) {
                        if (i == j) {
                            imageViews[j].setImageResource(selectedResources[j])
                            selectedColor = i
                            Log.d("wherestar", "i == j ${circleColors[j]}")
                        }
                        else {
                            imageViews[j].setImageResource(R.drawable.none_circle)
                            imageViews[j].drawable.setColorFilter(Color.parseColor(colorArr[j + 3]), PorterDuff.Mode.SRC_IN)
                            Log.d("wherestar", "i != j ${circleColors[j]}")

                        }
                    }

                }
            }
        }

        for (i in categoryArr) {
            for (j in 0..4) {
                if (i.color == colorArr[j + 3]) {
                    Log.d("clickablee", "${i.title} == ${colorArr[j+ 3]}")
                    imageViews[j].setImageResource(checkedResources[j])
                    imageViews[j].isClickable = false
                }
            }
        }

        for (i in imageViews.indices) {
            Log.d("clickablee", "${i + 3} -> ${imageViews[i].isClickable}")
        }

        for (i in imageViews.indices) {
            if (imageViews[i].isClickable) {
                imageViews[i].setImageResource(selectedResources[i])
                selectedColor = i
                break
            }
        }


    }

}