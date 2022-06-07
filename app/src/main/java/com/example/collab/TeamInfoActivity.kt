package com.example.collab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityTeamInfoBinding

class TeamInfoActivity : AppCompatActivity() {
    lateinit var data : TeamData
    lateinit var binding : ActivityTeamInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info)
//        data = intent.getSerializableExtra("data") as TeamData
//        binding.apply{
//            teamName.setText(data.toString())
//        }
    }
}