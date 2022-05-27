package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityTeamProjectBinding

class TeamProjectActivity : AppCompatActivity() {
    lateinit var binding : ActivityTeamProjectBinding
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
    }

    private fun initlayout() {
        binding.apply{
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