package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityCreateTeamBinding
import com.example.collab.databinding.ActivityPersonalCalendarBinding

class CreateTeamActivity : AppCompatActivity() {
    lateinit var binding : ActivityCreateTeamBinding
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
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
                var intent = Intent(context, TeamProjectActivity::class.java)
                startActivity(intent)
            }
            createTeamTabMenu.setOnClickListener {

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