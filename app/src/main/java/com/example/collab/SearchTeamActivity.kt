package com.example.collab


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.ActivityTeamSearchBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_team_search.*
import kotlinx.android.synthetic.main.team_info_row.*
import kotlinx.android.synthetic.main.team_info_row.view.*
import kotlin.collections.ArrayList


class SearchTeamActivity : AppCompatActivity() {
    lateinit var binding : ActivityTeamSearchBinding
    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: LinearLayoutManager
    var firestore : FirebaseFirestore ?= null
    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rdb = Firebase.database.getReference("Team")
        firestore = FirebaseFirestore.getInstance()
        teamRecyclerView.adapter = SearchTeamAdapter()
        teamRecyclerView.layoutManager = LinearLayoutManager(this)
        initlayout()
        teamSearchBtn.setOnClickListener {
            (teamRecyclerView.adapter as SearchTeamAdapter).searchTeam(teamSearchEdit.text.toString())
        }
    }



    
    inner class SearchTeamAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var teamInfo: ArrayList<TeamData> = arrayListOf()


        init {
            firestore?.collection("Team")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    teamInfo.clear()
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(TeamData::class.java)
                        teamInfo.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view =
                LayoutInflater.from(parent.context).inflate(R.layout.team_info_row, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
            viewHolder.teamName.text = teamInfo[position].teamName
            viewHolder.teamSubject.text = teamInfo[position].Subject
        }

        override fun getItemCount(): Int {
            return teamInfo.size
        }

        fun searchTeam(searchWord: String) {
            firestore?.collection("Team")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    teamInfo.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        if (snapshot.toString().contains(searchWord)) {
                            var item = snapshot.toObject(TeamData::class.java)
                            teamInfo.add(item!!)
                        }
                        else{
                            Toast.makeText(
                                applicationContext,
                                "검색 결과가 없습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    notifyDataSetChanged()
                }

        }
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

    }
