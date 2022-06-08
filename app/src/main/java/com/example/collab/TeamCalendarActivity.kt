package com.example.collab

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamCalendarBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_create_plan.*
import kotlinx.android.synthetic.main.activity_create_plan.view.*
import java.util.ArrayList

class TeamCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityTeamCalendarBinding
    var iteamName:String = ""
    val calendarData: ArrayList<CalendarTeamData> = ArrayList()
    val tmpData: ArrayList<CalendarTeamData> = ArrayList()
    val tmpData2: ArrayList<CalendarTeamData> = ArrayList()
    var firestore : FirebaseFirestore?= null
    var context = this

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
            teamName.text = iteamName
            teamPlanAddBtn.setOnClickListener {
                addTeamPalnDlg()
            }
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

    fun addTeamPalnDlg(){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.activity_create_plan)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()


        val planTitle = dialog.findViewById<EditText>(R.id.planTitle)
        val planStartTime = dialog.findViewById<EditText>(R.id.planStartTime)
        val planFinishTime = dialog.findViewById<EditText>(R.id.planFinishTime)



        dialog.findViewById<CalendarView>(R.id.calendar).setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val start = year.toString()+"-"+(month+1).toString()+"-"+dayOfMonth.toString()+"/12:00"
            val end =year.toString()+"-"+(month+1).toString()+"-"+(dayOfMonth+1).toString()+"/12:00"
            planStartTime.setText(start)
            planFinishTime.setText(end)
            updateRecyclerVeiw(start,end)
        }

        dialog.findViewById<Button>(R.id.detailWorkAddBtn).setOnClickListener {
            val plan = planTitle.text.toString()+"!"+planStartTime.text.toString()+"!"+planFinishTime.text.toString()
            firestore?.collection("Team")
                ?.document(iteamName)
                ?.get()?.addOnSuccessListener {
                    if(it.contains("plans")==true){
                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.update("plans", FieldValue.arrayUnion(plan))
                    }else{
                        val docData = hashMapOf(
                            "plans" to arrayListOf(plan)
                        )
                        firestore?.collection("Team")
                            ?.document(iteamName)
                            ?.set(docData, SetOptions.merge())
                    }
                }
            dialog.dismiss()
        }

        dialog.findViewById<ImageView>(R.id.createCancelBtn).setOnClickListener {
            dialog.dismiss()
        }

    }

    fun updateRecyclerVeiw(start:String, end:String){


    }


}