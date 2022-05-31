package com.example.collab

import PersonalCalendarAdapter
import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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

    lateinit var planDialog: PlanDialog


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
//        Toast.makeText(applicationContext, "onResume() 실행", Toast.LENGTH_SHORT).show()
//        initlayout()
//        initCal()
//        initCalendarData()
//        initRecyclerView()
//        initDialog()
    }

    private fun initDialog() {
        Toast.makeText(applicationContext, "일정 추가", Toast.LENGTH_SHORT).show()
        binding.personalPlanAddBtn.setOnClickListener {
            val dialog = PlanDialog(this)
            dialog.showDialog()
            dialog.setOnClickListener(object : PlanDialog.OnDialogClickListener {
                override fun onClicked(name: String) {
                    binding.titleTextView.text = name
                }
            })
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
            /**
             * 데이터베이스 연결
             */
            val val1 = scan.nextLine()
            val val2 = scan.nextLine()
            val val3 = scan.nextLine()
            calendarData.add(CalendarData(val1, val2, val3))
        }
    }


    private fun initRecyclerView() {
        Toast.makeText(applicationContext, "initRecyclerView()", Toast.LENGTH_SHORT).show()
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
                startActivity(intent)
            }
            teamProjectTabMenu.setOnClickListener {
                var intent = Intent(context, TeamProjectActivity::class.java)
                startActivity(intent)
            }
            createTeamTabMenu.setOnClickListener {
                var intent = Intent(context, CreateTeamActivity::class.java)
                startActivity(intent)
            }
            calendarTabMenu.setOnClickListener {

            }
            profileTabMenu.setOnClickListener {
                var intent = Intent(context, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
//        ) {
//            override fun onMove(
//                p0: RecyclerView,
//                p1: RecyclerView.ViewHolder,
//                p2: RecyclerView.ViewHolder
//            ): Boolean {
//                adapter.moveItem(p1.adapterPosition, p2.adapterPosition)
//                return true
//            }
//
//            override fun onSwiped(
//                viewHolder: RecyclerView.ViewHolder,
//                direction: Int
//            ) {
//                adapter.removeItem(viewHolder.adapterPosition)
//            }
//
//        }
//
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(personalCalendarRecyclerView)
}