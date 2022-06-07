package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.collab.databinding.ActivityCreateTeamBinding
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateTeamActivity : AppCompatActivity() {
    lateinit var binding : ActivityCreateTeamBinding
    val db = Firebase.firestore
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
        init()
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
    private fun init(){
        binding.apply{
            teamCreateBtn.setOnClickListener {
                val item = TeamData(
                    teamNameEdit.text.toString(),
                    subjectEdit.text.toString(),
                    subjectDetailEdit.text.toString(),
                    startTimeEdit.text.toString().toInt(),
                    finishTimeEdit.text.toString().toInt(),
                    memberCountEdit.text.toString().toInt()
                )

                db.collection("Team").document(teamNameEdit.text.toString())
                    .set(item)
                    .addOnSuccessListener {
                        teamNameEdit.text.clear()
                        subjectEdit.text.clear()
                        subjectDetailEdit.text.clear()
                        startTimeEdit.text.clear()
                        finishTimeEdit.text.clear()
                        memberCountEdit.text.clear()
                    }

            }
        }

    }
}