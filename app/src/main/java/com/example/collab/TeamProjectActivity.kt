package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamProjectBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_team_search.*

class TeamProjectActivity : AppCompatActivity() {
    lateinit var binding : ActivityTeamProjectBinding
    lateinit var adapter : TeamProjectAdapter
    var context = this
    var teamProject: ArrayList<TeamProject> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
    }

    private fun initlayout() {

        adapter = TeamProjectAdapter(teamProject)
        adapter.itemClickListener = object: TeamProjectAdapter.OnItemClickListener{
            override fun OnItemClick(data: TeamProject, position: Int) {
                Intent(this@TeamProjectActivity,DashBoardActivity::class.java).apply{
                    putExtra("teamName", data.teamName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run{startActivity(this)}
            }
        }

        binding.apply{

            belongTeamRecyclerView.adapter = adapter
            belongTeamRecyclerView.layoutManager = LinearLayoutManager(context)
            // 화면전환
            teamSearchTabMenu.setOnClickListener {
                var intent = Intent(context, SearchTeamActivity::class.java)
                startActivity(intent)
            }
            teamProjectTabMenu.setOnClickListener{

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
                var intent = Intent(context, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }
}