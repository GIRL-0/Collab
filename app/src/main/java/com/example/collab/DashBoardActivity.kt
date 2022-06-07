package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityDashBoardBinding
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
            var teamName = intent.getStringExtra("teamName")

        binding.apply{
            projectName.text = teamName

            progressLayout.setOnClickListener {
                Intent(this@DashBoardActivity,WorkActivity::class.java).apply{
                    putExtra("teamName", teamName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run{startActivity(this)}
            }
            teamSetting.setOnClickListener{
                Intent(this@DashBoardActivity,ManageTeamActivity::class.java).apply{
                    putExtra("teamName", teamName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run{startActivity(this)}
            }



        }
    }
}