package com.example.collab

import android.app.Dialog
import android.content.ContentValues
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.collab.databinding.ActivityManageTeamBinding
import com.google.firebase.firestore.FieldValue
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
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init(){
        var teamname = intent.getStringExtra("teamName")
        binding.teamName.text = teamname
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
        adapter.itemClickListener = object:ManageAdapter.OnItemClickListener{
            override fun OnItemClick(data: UserData, isMember: Boolean) {
                if(isMember){
                    val dialog = Dialog(context)
                    dialog.setContentView(R.layout.manage_member_dialog)
                    dialog.window!!.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setCanceledOnTouchOutside(true)
                    dialog.setCancelable(true)
                    dialog.show()

                    dialog.findViewById<TextView>(R.id.userName).text = data.name
                    dialog.findViewById<TextView>(R.id.userMajorTag).text = data.field
                    dialog.findViewById<TextView>(R.id.userIntroduce).text = data.introduction
                    if (data.rating == null){// rating 이 null 인경우
                        dialog.findViewById<LinearLayout>(R.id.userGrade).visibility = LinearLayout.GONE
                    }else{
                        dialog.findViewById<TextView>(R.id.userGradeNum).text = data.rating
                    }

                    dialog.findViewById<Button>(R.id.KickOutBtn).setOnClickListener {
                        firestore?.collection("Team")
                            ?.document(teamname!!)
                            ?.update("member", FieldValue.arrayRemove(data.email))
                        firestore?.collection("User")
                            ?.document(data.email!!)
                            ?.update("teams",FieldValue.arrayRemove(teamname!!))
                        dialog.dismiss()
                    }
                    dialog.findViewById<ImageView>(R.id.workAddCancelBtn).setOnClickListener {
                        dialog.dismiss()
                    }

                }else{
                    val dialog = Dialog(context)
                    dialog.setContentView(R.layout.request_member_dialog)
                    dialog.window!!.setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                    )
                    dialog.setCanceledOnTouchOutside(true)
                    dialog.setCancelable(true)
                    dialog.show()

                    dialog.findViewById<TextView>(R.id.userName).text = data.name
                    dialog.findViewById<TextView>(R.id.userMajorTag).text = data.field
                    dialog.findViewById<TextView>(R.id.userIntroduce).text = data.introduction
                    if (data.rating == null){// rating 이 null 인경우
                        dialog.findViewById<LinearLayout>(R.id.userGrade).visibility = LinearLayout.GONE
                    }else{
                        dialog.findViewById<TextView>(R.id.userGradeNum).text = data.rating
                    }

                    dialog.findViewById<Button>(R.id.acceptBtn).setOnClickListener{
                        firestore?.collection("Team")
                            ?.document(teamname!!)
                            ?.update("join", FieldValue.arrayRemove(data.email))
                        firestore?.collection("Team")
                            ?.document(teamname!!)
                            ?.update("member", FieldValue.arrayUnion(data.email))
                        firestore?.collection("User")
                            ?.document(data.email!!)
                            ?.update("teams",FieldValue.arrayUnion(teamname!!))
                        dialog.dismiss()
                    }
                    dialog.findViewById<Button>(R.id.rejectBtn).setOnClickListener {
                        firestore?.collection("Team")
                            ?.document(teamname!!)
                            ?.update("join", FieldValue.arrayRemove(data.email))
                        dialog.dismiss()
                    }

                    dialog.findViewById<ImageView>(R.id.workAddCancelBtn).setOnClickListener {
                        dialog.dismiss()
                    }

                }
            }

        }
        binding.teamMemberRecyclerView.adapter = adapter
        binding.teamMemberRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}