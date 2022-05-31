package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamProjectBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TeamProjectActivity : AppCompatActivity() {
    lateinit var binding : ActivityTeamProjectBinding
    lateinit var layoutManager : LinearLayoutManager
    lateinit var adapter : TeamProjectAdapter
    lateinit var rdb : DatabaseReference
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
    }

    private fun initlayout() {
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rdb = Firebase.database.getReference("Team")
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<TeamProject>()
            .setQuery(query,TeamProject::class.java)
            .build()

        adapter = TeamProjectAdapter(option)
        adapter.itemClickListener = object:TeamProjectAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {

            }

        }
        binding.apply{
            belongTeamRecyclerView.layoutManager = layoutManager
            belongTeamRecyclerView.adapter = adapter
            adapter.startListening()



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