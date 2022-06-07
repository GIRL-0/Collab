package com.example.collab

import PersonalCalendarAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.UserInfo.userInfoName
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
        binding.userName.text = userInfoName
        Log.d("life_cycle", "onCreate")
//        Toast.makeText(applicationContext, "onCreate() 실행", Toast.LENGTH_SHORT).show()
        initlayout()
        initCal()
        initCalendarData()
//        initRecyclerView()
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
            //TODO: 데이터베이스에 푸쉬하고 가져오기
            val db = Firebase.firestore
            val washingtonRef = db.collection("User").document(userInfoEmail)
            washingtonRef.update("plans", FieldValue.arrayUnion("3"))
//            val data = hashMapOf("plans" to arrayListOf(2, 2, 4),)
//            data.put("plans",)
//            db.collection("User").document(UserInfo.userInfoEmail)
//                .set(data, SetOptions.merge())
            val val1 = scan.nextLine()
            val val2 = scan.nextLine()
            val val3 = scan.nextLine()
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
               // Toast.makeText(applicationContext, data.planDate, Toast.LENGTH_SHORT).show()
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
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            titleTextView.setOnClickListener {
                Toast.makeText(applicationContext, "Signout", Toast.LENGTH_SHORT).show()
            }

        }
    }


}