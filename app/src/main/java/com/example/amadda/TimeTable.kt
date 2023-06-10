package com.example.amadda

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.ActivityProfileBinding
import com.example.amadda.databinding.ActivityTimeTableBinding

class TimeTable : AppCompatActivity() {
    lateinit var binding: ActivityTimeTableBinding
    lateinit var timetableAdapter: TimeTableAdapter
    val data: ArrayList<TimeTableData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    @SuppressLint("MissingInflatedId")
    fun init(){
        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        timetableAdapter = TimeTableAdapter(data)

        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
        binding.addTimeTable.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_timetable, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val  mAlertDialog = mBuilder.show()

            val okButton = mDialogView.findViewById<Button>(R.id.addButton)
            okButton.setOnClickListener {
                val className = mDialogView.findViewById<EditText>(R.id.className).text
                val professor = mDialogView.findViewById<EditText>(R.id.professor).text
                val classRoom = mDialogView.findViewById<EditText>(R.id.classRoom).text
                val list = arrayListOf<String>()
                val newData = TimeTableData(className.toString(),professor.toString(), classRoom.toString(),list,"","")
                timetableAdapter.addItem(newData)
                mAlertDialog.dismiss()
            }
            binding.recyclerView.adapter = timetableAdapter

          /*  val noButton = mDialogView.findViewById<Button>(R.id.closeButton)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }*/
        }
    }

}