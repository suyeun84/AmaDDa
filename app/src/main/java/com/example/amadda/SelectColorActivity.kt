package com.example.amadda

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.amadda.databinding.ActivitySelectColorBinding


class SelectColorActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectColorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectColorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout();
        initBtn();
    }

    fun initLayout() {
        var imageView8 = binding.imageView8
        imageView8.setImageResource(R.drawable.ic_baseline_check_circle_24)
        imageView8.imageTintList = ColorStateList.valueOf(Color.parseColor("#4C4C67"))

    }

    fun initBtn(){
        val imgView = binding.imageView
        binding.imageView.setOnClickListener {
            if (resources.getIdentifier(
                    "ic_baseline_circle_24", "drawable",
                    packageName
                ) == R.drawable.ic_baseline_circle_24) {
                imgView.setImageResource(R.drawable.ic_baseline_check_circle_24)
            } else {
                imgView.setImageResource(R.drawable.ic_baseline_circle_24)

            }
        }

    }

}