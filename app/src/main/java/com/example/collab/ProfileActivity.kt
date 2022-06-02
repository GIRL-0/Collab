package com.example.collab

import PersonalCalendarAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.ActivityProfileBinding
import java.util.ArrayList

class ProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityProfileBinding
    lateinit var profileNoticeRecyclerView: RecyclerView
    val profileNoticeData: ArrayList<ProfileNoticeData> = ArrayList()
    var context = this
    lateinit var adapter: ProfileNoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        Toast.makeText(applicationContext, "initProfileRecyclerView()", Toast.LENGTH_SHORT).show()
        profileNoticeRecyclerView = findViewById(R.id.profileAlarmRecyclerView)
        profileNoticeRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        adapter = ProfileNoticeAdapter(profileNoticeData)
        profileNoticeRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ProfileNoticeAdapter.OnItemClickListener {
            override fun OnItemClick(data: ProfileNoticeData) {
                Toast.makeText(applicationContext, data.alarmContent, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initlayout() {
        binding.apply{
            teamSearchTabMenu.setOnClickListener {
                var intent = Intent(context, SearchTeamActivity::class.java)
                startActivity(intent)
            }
            teamProjectTabMenu.setOnClickListener{
                var intent = Intent(context, TeamProjectActivity::class.java)
                startActivity(intent)
            }
            createTeamTabMenu.setOnClickListener {
                var intent = Intent(context, CreateTeamActivity::class.java)
                startActivity(intent)
            }
            calendarTabMenu.setOnClickListener {
                var intent = Intent(context, PersonalCalendarActivity::class.java)
                startActivity(intent)
            }
            profileTabMenu.setOnClickListener {

            }
        }
    }
}
