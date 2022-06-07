package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamInfoBinding
import kotlinx.android.synthetic.main.activity_team_info.*

class TeamInfoActivity : AppCompatActivity() {

    lateinit var adapter: TeamInfoAdapter
    lateinit var binding : ActivityTeamInfoBinding
    var userinfo: ArrayList<UserData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cancel_button.setOnClickListener{
            var intent = Intent(this@TeamInfoActivity, SearchTeamActivity::class.java)
            startActivity(intent)
        }
        init()
    }

    private fun init(){
        val teamname = intent.getStringExtra("teamName")
        teamName.text = teamname
        projectSubject.text = intent.getStringExtra("Subject")
        subjectDetail.text = intent.getStringExtra("SubjectDetail")
        projectStartTime.text = intent.getIntExtra("TimeStart",0).toString()
        projectfinishTime.text = intent.getIntExtra("TimeFinish",0).toString()

        adapter = TeamInfoAdapter(userinfo,teamname.toString())
        binding.teamMemberRecyclerView.adapter = adapter
        binding.teamMemberRecyclerView.layoutManager = LinearLayoutManager(this)
    }

}