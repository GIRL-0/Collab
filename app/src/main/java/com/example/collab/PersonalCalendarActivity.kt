package com.example.collab

import PersonalCalendarAdapter
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.ArrayList


class PersonalCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityPersonalCalendarBinding
    lateinit var personalCalendarRecyclerView: RecyclerView
    val calendarData: ArrayList<CalendarData> = ArrayList()
    val tmpData: ArrayList<CalendarData> = ArrayList()
    var context = this
    lateinit var adapter: PersonalCalendarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("life_cycle", "onCreate")
        initlayout()
        initCal()
        initCalendarData()
        initDialog()
        initRecyclerView()
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
            dialog.onDismissedClickListener(object :
                PersonalCalendarPlanDialog.onPlanCreateClickListener {
                override fun onPlanCreateClick(name: String) {
                    initRecyclerView()
                }
            })
        }
    }

    private fun initCal() {
        binding.personalCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var toast = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            Toast.makeText(applicationContext, toast, Toast.LENGTH_SHORT).show()

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
            Toast.makeText(
                applicationContext,
                "initCalendarRecyclerView()",
                Toast.LENGTH_SHORT
            ).show()
            personalCalendarRecyclerView =
                findViewById(R.id.personalPlanRecyclerView)
            personalCalendarRecyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = PersonalCalendarAdapter(tmpData)
            personalCalendarRecyclerView.adapter = adapter
            adapter.itemClickListener =
                object : PersonalCalendarAdapter.OnItemClickListener {
                    override fun OnItemClick(data: CalendarData) {
                        Toast.makeText(
                            applicationContext,
                            data.planContent,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun initCalendarData() {
        //데이터베이스에 푸쉬하고 가져오기
//            val db = Firebase.firestore
//            val docRef = db.collection("User").document(userInfoEmail)
//            docRef.get()
//                .addOnSuccessListener { document ->
//                    if (document != null) {
//                        var planData = document.get("plans") as ArrayList<String>
//                        for (plan in planData) {
//                            val container = plan.split("!")
//                            calendarData.add(CalendarData(container[0], container[1].split("/")[0], container[1].split("/")[1],
//                                container[2].split("/")[0], container[2].split("/")[1]))
//                            Log.d("calendarData 테스트", calendarData.toString())
//
//                        }
//                        Log.d("테스트", planData.toString())
//                    } else {
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(ContentValues.TAG, "get failed with ", exception)
//                }
//            doc.update("plans", FieldValue.arrayUnion("3"))

//            val data = hashMapOf("plans" to arrayListOf(2, 2, 4),)
//            data.put("plans",)
//            db.collection("User").document(UserInfo.userInfoEmail)
//                .set(data, SetOptions.merge())
    }


    private fun initRecyclerView() {
        val db = Firebase.firestore
        val docRef = db.collection("User").document(userInfoEmail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document?.get("plans") != null) {
                    var planData = document.get("plans") as ArrayList<String>
                    for (plan in planData) {
                        val container = plan.split("!")
                        calendarData.add(
                            CalendarData(
                                container[0],
                                container[1].split("/")[0],
                                container[1].split("/")[1],
                                container[2].split("/")[0],
                                container[2].split("/")[1]
                            )
                        )
                        Toast.makeText(
                            applicationContext,
                            "initCalendarRecyclerView()",
                            Toast.LENGTH_SHORT
                        ).show()
                        personalCalendarRecyclerView =
                            findViewById(R.id.personalPlanRecyclerView)
                        personalCalendarRecyclerView.layoutManager = LinearLayoutManager(
                            this,
                            LinearLayoutManager.VERTICAL, false
                        )
                        adapter = PersonalCalendarAdapter(calendarData)
                        personalCalendarRecyclerView.adapter = adapter
                        adapter.itemClickListener =
                            object : PersonalCalendarAdapter.OnItemClickListener {
                                override fun OnItemClick(data: CalendarData) {
                                    Toast.makeText(
                                        applicationContext,
                                        data.planContent,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                    Log.d("calendarData 테스트", calendarData.toString())
                    Log.d("calendarData 테스트", calendarData[0].planContent.toString())
                } else {
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
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
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            titleTextView.setOnClickListener {
                Toast.makeText(applicationContext, "Signout", Toast.LENGTH_SHORT).show()
            }
        }
    }


}