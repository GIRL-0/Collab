package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityTeamInfoBinding
import kotlinx.android.synthetic.main.activity_team_info.*

class TeamInfoActivity : AppCompatActivity() {
    lateinit var teamdata : TeamData
    lateinit var binding : ActivityTeamInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info)
        teamName.text = intent.getStringExtra("teamName")
        projectSubject.text = intent.getStringExtra("Subject")
        cancel_button.setOnClickListener{
            var intent = Intent(this@TeamInfoActivity, SearchTeamActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun init(){
//        teamName.text = teamdata.teamName
//    }
}