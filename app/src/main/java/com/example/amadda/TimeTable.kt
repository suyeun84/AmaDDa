package com.example.amadda

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.amadda.databinding.ActivityProfileBinding
import com.example.amadda.databinding.ActivityTimeTableBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TimeTable : AppCompatActivity() {
    lateinit var binding: ActivityTimeTableBinding
    lateinit var timetableAdapter: TimeTableAdapter
    val data: ArrayList<TimeTableData> = ArrayList()
    lateinit var rdb: DatabaseReference
    var userId: String = ""
    val data:ArrayList<Lecture> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("userId").toString()
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var lecArr : ArrayList<Int> = ArrayList<Int>()
        lecArr.add(1)
        lecArr.add(4)
        var lecArr2 : ArrayList<Int> = ArrayList<Int>()
        lecArr2.add(2)
        lecArr2.add(3)
        var lecArr3 : ArrayList<Int> = ArrayList<Int>()
        lecArr3.add(0)
        data.add(Lecture("모프", "지정희", "공A1510", lecArr, "10:30", "12:00"))
        data.add(Lecture("운체", "진현욱", "공C487", lecArr, "12:00", "13:30"))
        data.add(Lecture("과기글", "김땡땡", "경영202", lecArr, "15:00", "18:00"))

        rdb = Firebase.database.getReference("Users/user/" + userId)
        Log.d("TimeTableLog", "1")
        rdb.child("timetableList").get().addOnSuccessListener { dataSnapshot ->
            Log.d("TimeTableLog", "2")
            GlobalScope.launch(Dispatchers.Main) {
                Log.d("TimeTableLog", "3")
                // 비동기 작업이 완료된 후에 실행될 코드
                if (dataSnapshot.exists()) {
                    data.clear()
                    Log.d("TimeTableLog", "4")
                    val listType = object : GenericTypeIndicator<ArrayList<Lecture>>() {}
                    val subArr = dataSnapshot.getValue(listType)

                    if (subArr != null) {
                        for (i in subArr.indices) {
                            data.add(subArr[i])
                            // notifyDataSetChanged (added?)
                            Log.d("TimeTableLog", subArr[i].toString())
                        }

                        rdb.child("timetableList").setValue(subArr)
                    }
                } else {
                    Log.d("TimeTableLog", "5")
                    saveLecture(data) {success ->
                        Log.d("TimeTableLog", "6")
                        if (success) {
                            // 데이터베이스에 변경된 값 저장 후 처리할 작업 수행
                            Log.d("saveSubscription", "canceled success")
                        } else {
                            // 저장 실패 처리
                            Log.d("saveSubscription", "canceled failed")
                        }
                    }

                }
                init() // 비동기 작업이 완료된 후에 init()를 호출
            }
        }
//        init()
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

    private fun saveLecture(subArr: ArrayList<Lecture>, completion: (Boolean) -> Unit) {
        rdb.child("timetableList").setValue(subArr)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(true)
                } else {
                    completion(false)
                }
            }
    }

}