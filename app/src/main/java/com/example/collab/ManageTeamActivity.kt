package com.example.collab

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.collab.databinding.ActivityManageTeamBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_manage_team.*
import kotlinx.android.synthetic.main.activity_manage_team.teamMemberRecyclerView
import kotlinx.android.synthetic.main.activity_team_info.*
import kotlinx.android.synthetic.main.activity_team_search.view.*

class ManageTeamActivity : AppCompatActivity() {
    lateinit var adapter: ManageAdapter
    lateinit var binding: ActivityManageTeamBinding
    var firestore : FirebaseFirestore?= null
    var userinfo: ArrayList<UserData> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init(){
        var teamname = intent.getStringExtra("teamName")
        binding.teamNameEdit.text = teamname
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Team")
            ?.document(teamname!!)
            ?.get()?.addOnSuccessListener {
                if(it?.contains("subject")==true){
                    binding.subjectEdit.setText(it?.get("subject") as String)
                }
                if(it?.contains("subjectDetail")==true){
                    binding.subjectDetailEdit.setText(it?.get("subjectDetail") as String)
                }
            }
        binding.editBtn.setOnClickListener {
            val dataDoc = hashMapOf(
                "subject" to binding.subjectEdit.text.toString(),
                "subjectDetail" to binding.subjectDetailEdit.text.toString()
            )
            firestore?.collection("Team")
                ?.document(teamname!!)
                ?.set(dataDoc, SetOptions.merge())

        }
        adapter = ManageAdapter(userinfo,teamname.toString())
        binding.teamMemberRecyclerView.adapter = adapter
        binding.teamMemberRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}