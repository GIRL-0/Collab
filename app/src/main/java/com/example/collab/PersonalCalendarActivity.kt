package com.example.collab

import PersonalCalendarAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import java.util.*


class PersonalCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonalCalendarBinding
    lateinit var personalCalendarRecyclerView: RecyclerView
    val calendarData: ArrayList<CalendarData> = ArrayList()
    var context = this
    lateinit var adapter: PersonalCalendarAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("life_cycle", "onCreate")
//        Toast.makeText(applicationContext, "onCreate() 실행", Toast.LENGTH_SHORT).show()
        initlayout()
        initCal()
        initCalendarData()
        initRecyclerView()
        initDialog()
    }

    override fun onResume() {
        super.onResume()
        Log.d("life_cycle", "onResume")
    }

    private fun initDialog() {
//        Toast.makeText(applicationContext, "일정 추가", Toast.LENGTH_SHORT).show()
        binding.personalPlanAddBtn.setOnClickListener {
            val dialog = PersonalCalendarPlanDialog(this)
            dialog.showDialog()
            dialog.onDismissedClickListener(object : PersonalCalendarPlanDialog.onPlanCreateClickListener {
                override fun onPlanCreateClick(name: String) {
//                    binding.titleTextView.text = name
                    initRecyclerView()}})
        }
    }

    private fun initCal() {
        binding.personalCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var toast = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCalendarData() {
        val scan = Scanner(resources.openRawResource(R.raw.words))
        while (scan.hasNextLine()) {
            //TODO: 데이터베이스 불러오기
            val val1 = scan.nextLine()
            val val2 = scan.nextLine()
            val val3 = scan.nextLine()
            calendarData.add(CalendarData(val1, val2, val3))
        }
    }


    private fun initRecyclerView() {
        Toast.makeText(applicationContext, "initCalendarRecyclerView()", Toast.LENGTH_SHORT).show()
        personalCalendarRecyclerView = findViewById(R.id.personalPlanRecyclerView)
        personalCalendarRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        adapter = PersonalCalendarAdapter(calendarData)
        personalCalendarRecyclerView.adapter = adapter
        adapter.itemClickListener = object : PersonalCalendarAdapter.OnItemClickListener {
            override fun OnItemClick(data: CalendarData) {
                Toast.makeText(applicationContext, data.planDate, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun initlayout() {
        binding.apply {
            teamSearchTabMenu.setOnClickListener {
                var intent = Intent(context, SearchTeamActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            teamProjectTabMenu.setOnClickListener {
                var intent = Intent(context, TeamProjectActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            createTeamTabMenu.setOnClickListener {
                var intent = Intent(context, CreateTeamActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            calendarTabMenu.setOnClickListener {

            }
            profileTabMenu.setOnClickListener {
                var intent = Intent(context, ProfileActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            titleTextView.setOnClickListener {
                var intent = Intent(context, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}