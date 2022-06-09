package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.databinding.ActivityTeamInfoBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_team_info.*
import kotlinx.android.synthetic.main.activity_team_info.view.*

class TeamInfoActivity : AppCompatActivity() {

    lateinit var adapter: TeamInfoAdapter
    lateinit var binding : ActivityTeamInfoBinding
    var userinfo: ArrayList<UserData> = arrayListOf()
    var data : ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        join_button.setOnClickListener {
            want_join()
        }

        cancel_button.setOnClickListener{
            var intent = Intent(this@TeamInfoActivity, SearchTeamActivity::class.java)
            startActivity(intent)
        }
        init()

    }

    fun init() {
        val teamname = intent.getStringExtra("teamName")
        teamName.text = teamname
        projectSubject.text = intent.getStringExtra("Subject")
        subjectDetail.text = intent.getStringExtra("SubjectDetail")
        projectStartTime.text = intent.getIntExtra("TimeStart", 0).toString()
        projectfinishTime.text = intent.getIntExtra("TimeFinish", 0).toString()

        adapter = TeamInfoAdapter(userinfo, teamname.toString())
        binding.teamMemberRecyclerView.adapter = adapter
        binding.teamMemberRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    fun want_join(){
        val teamname = intent.getStringExtra("teamName")
        val membercount = intent.getIntExtra("MemberCount",0)
        val db = FirebaseFirestore.getInstance()

        db.collection("Team")
            .document(teamname!!)
            .addSnapshotListener { querySnapshot, _ ->
                data = querySnapshot?.get("member") as ArrayList<String>
                val joinnum = data!!.size
                Log.i("data",data.toString())
                for (member in data!!){
                    if(userInfoEmail != member && joinnum < membercount){
                        // 이미 기존 참여 팀 아닌지, 팀원 수 다 안찼는지 체크
                        db.collection("Team")
                            .document(teamname!!)
                            .get().addOnSuccessListener {
                                if(it?.contains("join")==true) {
                                    val docRef = db.collection("Team").document(teamname!!)
                                    docRef.update("join", FieldValue.arrayUnion(userInfoEmail))
                                    Toast.makeText(this,"신청 완료 !",Toast.LENGTH_SHORT).show() }
                                else{
                                    val docData = hashMapOf(
                                        "join" to arrayListOf(userInfoEmail)
                                    )
                                    db.collection("Team")
                                        .document(teamname!!)
                                        .set(docData, SetOptions.merge())
                                    Toast.makeText(this,"신청 완료 !",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                    else{
                        Toast.makeText(this,"참여할 수 없습니다.",Toast.LENGTH_SHORT).show()
                    }
                }


            }
    }

}