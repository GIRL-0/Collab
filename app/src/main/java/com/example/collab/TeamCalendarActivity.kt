package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamCalendarBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class TeamCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityTeamCalendarBinding
    var iteamName:String = ""
    val calendarData: ArrayList<CalendarTeamData> = ArrayList()
    val tmpData: ArrayList<CalendarTeamData> = ArrayList()
    var firestore : FirebaseFirestore?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initCal()
    }

    private fun init() {
        iteamName = intent.getStringExtra("teamName")!!
        binding.apply{
            this.teamName.text = iteamName

        }
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(iteamName)
            ?.addSnapshotListener { value, error ->
                if(value?.contains("plans")==true){
                    val list = value?.get("plans") as ArrayList<String>
                    for(str in list!!){
                        val container = str.split("!")
                        calendarData.add(CalendarTeamData(
                            container[0],
                            container[1].split("/")[0],
                            container[1].split("/")[1],
                            container[2].split("/")[0],
                            container[2].split("/")[1],
                            iteamName))}
                }
                if(value?.contains("member")==true){
                    val list = value?.get("member") as ArrayList<String>
                    for(str in list!!){
                        firestore?.collection("User")
                            ?.document(str)
                            ?.addSnapshotListener { value2, error ->
                                if(value2?.contains("plans")==true){
                                    val list2 = value2?.get("plans") as ArrayList<String>
                                    val name = value2?.get("name") as String
                                    for(str2 in list2!!){
                                        val container = str2.split("!")
                                        calendarData.add(CalendarTeamData(
                                            container[0],
                                            container[1].split("/")[0],
                                            container[1].split("/")[1],
                                            container[2].split("/")[0],
                                            container[2].split("/")[1],
                                            name)) }
                                }
                            }
                    }
                }
                Log.i("test", calendarData.toString())
            }
    }

    private fun initCal() {
        binding.teamCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->

            tmpData.clear()
            for (data in calendarData) {
                val selectStartYear = data.planStartDate.split("-")[0].toInt()
                val selectStartMonth = data.planStartDate.split("-")[1].toInt()
                val selectStartDay = data.planStartDate.split("-")[2].toInt()
                val selectEndYear = data.planEndDate.split("-")[0].toInt()
                val selectEndMonth = data.planEndDate.split("-")[1].toInt()
                val selectEndDay = data.planEndDate.split("-")[2].toInt()

                if (selectStartYear <= year && selectStartMonth <= month + 1 && selectStartDay <= dayOfMonth
                    && year <= selectEndYear && month + 1 <= selectEndMonth && dayOfMonth <= selectEndDay
                ) {
                    tmpData.add(data)
                }

            }

            val adapter = TeamCalendarAdapter(tmpData)
            binding.teamPlanRecyclerView.adapter = adapter
            binding.teamPlanRecyclerView.layoutManager = LinearLayoutManager(this)

        }
    }


}