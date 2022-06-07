package com.example.collab


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.databinding.ActivityTeamSearchBinding
import com.example.collab.databinding.TeamInfoRowBinding
import kotlinx.android.synthetic.main.activity_team_search.*
import kotlinx.android.synthetic.main.team_info_row.*
import kotlin.collections.ArrayList


class SearchTeamActivity : AppCompatActivity() {
    lateinit var binding : ActivityTeamSearchBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: SearchTeamAdapter
    // var firestore : FirebaseFirestore ?= null
    var context = this
    var teamInfo: ArrayList<TeamData> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //firestore = FirebaseFirestore.getInstance()
        init()
        initlayout()
        teamSearchBtn.setOnClickListener {
            (teamRecyclerView.adapter as SearchTeamAdapter).searchTeam(teamSearchEdit.text.toString())
        }
    }

    private fun init(){
//        binding.teamRecyclerView.layoutManager = LinearLayoutManager(this,
//            LinearLayoutManager.VERTICAL,false)
        adapter = SearchTeamAdapter(teamInfo)
        adapter.itemClickListener = object: SearchTeamAdapter.OnItemClickListener{
            override fun OnItemClick(data: TeamData, position : Int) {
                Intent(this@SearchTeamActivity, TeamInfoActivity::class.java).apply {
                        putExtra("teamName", data.teamName)
                        putExtra("Subject", data.Subject)
                        putExtra("SubjectDetail", data.SubjectDetail)
                        putExtra("TimeStart", data.TimeStart)
                        putExtra("TimeFinish", data.TimeFinish)
                    putExtra("MemberCount",data.MemberCount)

                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }

            }
        }
        //binding.teamRecyclerView.adapter = SearchTeamAdapter(teamInfo)
        binding.teamRecyclerView.adapter = adapter
        binding.teamRecyclerView.layoutManager = LinearLayoutManager(this)
    }


        private fun initlayout() {

            binding.apply {

                teamSearchTabMenu.setOnClickListener {

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
                    var intent = Intent(context, PersonalCalendarActivity::class.java)
                    startActivity(intent)
                }
                profileTabMenu.setOnClickListener {
                    var intent = Intent(context, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        //teamRecyclerView.adapter = SearchTeamAdapter(teamInfo)
        //teamRecyclerView.layoutManager = LinearLayoutManager(this)

    }
