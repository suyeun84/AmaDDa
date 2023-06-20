package com.example.amadda

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable.Orientation
import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amadda.databinding.ActivityTimeTableBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    lateinit var hour_adapter: ArrayAdapter<String>
    lateinit var minute_adapter: ArrayAdapter<String>


    val hour: ArrayList<String> =
        arrayListOf("08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18")
    val minute: ArrayList<String> = arrayListOf("00", "10", "20", "30", "40", "50")

    lateinit var startHour: String
    lateinit var startMinute: String
    lateinit var endHour: String
    lateinit var endMinute: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = intent.getStringExtra("userId").toString()
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 확인을 위해 자동으로 데이터 추가하게끔 해뒀음
        // 실제 구현 시 주석 처리 필요
//        var lecArr: ArrayList<Int> = ArrayList<Int>()
//        lecArr.add(1)
//        lecArr.add(4)
//        var lecArr2: ArrayList<Int> = ArrayList<Int>()
//        lecArr2.add(2)
//        lecArr2.add(3)
//        var lecArr3: ArrayList<Int> = ArrayList<Int>()
//        lecArr3.add(0)
//        data.add(TimeTableData("모프", "지정희", "공A1510", lecArr, "10:30", "12:00"))
//        data.add(TimeTableData("운체", "진현욱", "공C487", lecArr2, "12:00", "13:30"))
//        data.add(TimeTableData("과기글", "김땡땡", "경영202", lecArr3, "15:00", "18:00"))

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
                    val listType = object : GenericTypeIndicator<ArrayList<TimeTableData>>() {}
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
                    saveLecture(data) { success ->
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
    }

    @SuppressLint("MissingInflatedId")
    fun init() {

        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        timetableAdapter = TimeTableAdapter(data)


        binding.backBtnAppbar.setOnClickListener {
            finish()
        }
        binding.addTimeTable.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_timetable, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)

            val mAlertDialog = mBuilder.show()

            hour_adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hour)
            minute_adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minute)

            mDialogView.findViewById<Spinner>(R.id.startHour).adapter = hour_adapter
            mDialogView.findViewById<Spinner>(R.id.startHour).onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        startHour = mDialogView.findViewById<Spinner>(R.id.startHour)
                            .getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

            mDialogView.findViewById<Spinner>(R.id.endHour).adapter = hour_adapter
            mDialogView.findViewById<Spinner>(R.id.endHour).onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        endHour = mDialogView.findViewById<Spinner>(R.id.endHour)
                            .getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

            mDialogView.findViewById<Spinner>(R.id.startMinute).adapter = minute_adapter
            mDialogView.findViewById<Spinner>(R.id.startMinute).onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        startMinute = mDialogView.findViewById<Spinner>(R.id.startMinute)
                            .getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

            mDialogView.findViewById<Spinner>(R.id.endMinute).adapter = minute_adapter
            mDialogView.findViewById<Spinner>(R.id.endMinute).onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        endMinute = mDialogView.findViewById<Spinner>(R.id.endMinute)
                            .getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }


            val okButton = mDialogView.findViewById<Button>(R.id.addButton)
            okButton.setOnClickListener {
                val className = mDialogView.findViewById<EditText>(R.id.className).text
                val professor = mDialogView.findViewById<EditText>(R.id.professor).text
                val classRoom = mDialogView.findViewById<EditText>(R.id.classRoom).text
                val mon = mDialogView.findViewById<CheckBox>(R.id.mon).isChecked
                val tue = mDialogView.findViewById<CheckBox>(R.id.tue).isChecked
                val wed = mDialogView.findViewById<CheckBox>(R.id.wed).isChecked
                val thu = mDialogView.findViewById<CheckBox>(R.id.thu).isChecked
                val fri = mDialogView.findViewById<CheckBox>(R.id.fri).isChecked
                val dayList = arrayListOf<Int>()

                var checkedDay = 0
                if (mon) {
                    checkedDay += 1
                    dayList.add(0)
                }
                if (tue) {
                    checkedDay += 1
                    dayList.add(1)
                }
                if (wed) {
                    checkedDay += 1
                    dayList.add(2)
                }
                if (thu) {
                    checkedDay += 1
                    dayList.add(3)
                }
                if (fri) {
                    checkedDay += 1
                    dayList.add(4)
                }
                if (className.toString() == "" || professor.toString() == "" || classRoom.toString() == "") {
                    Toast.makeText(this, "과목정보를 정확하게 입력해주세요", Toast.LENGTH_SHORT).show()
                } else if (checkedDay > 2) {
                    Toast.makeText(this, "요일은 최대 2개까지 선택할 수 있습니다", Toast.LENGTH_SHORT).show()
                } else if (checkedDay == 0) {
                    Toast.makeText(this, "요일을 1개 이상 선택하세요", Toast.LENGTH_SHORT).show()
                } else {
                    // 요일 버튼 리스너 추가해야 함
                    // 요일 최소 1개 ~ 최대 2개 선택하도록
                    // 그리고 mon, tue, wed, thu, fri -> {0, 1, 2, 3, 4}로 숫자 배정해서
                    // list.add(int)로 넣어주세여
                    val newData = TimeTableData(
                        className.toString(), professor.toString(),
                        classRoom.toString(), dayList,
                        startHour + ":" + startMinute, endHour + ":" + endMinute
                    )
                    timetableAdapter.addItem(newData)
                    mAlertDialog.dismiss()
                    timetableAdapter.notifyDataSetChanged()
                    rdb.child("timetableList").setValue(timetableAdapter.items)
                }
            }


            /*  val noButton = mDialogView.findViewById<Button>(R.id.closeButton)
              noButton.setOnClickListener {
                  mAlertDialog.dismiss()
              }*/
        }

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.recyclerView.adapter = timetableAdapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

    }

    val simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            timetableAdapter.removeItem(viewHolder.adapterPosition)
        }
    }
    val itemTouchHelper = ItemTouchHelper(simpleCallback)


    private fun saveLecture(subArr: ArrayList<TimeTableData>, completion: (Boolean) -> Unit) {
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