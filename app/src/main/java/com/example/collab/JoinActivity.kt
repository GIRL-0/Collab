package com.example.collab

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.databinding.ActivityJoinBinding
import com.example.collab.databinding.ActivityLoginBinding
import com.example.collab.databinding.ActivityProfileBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    lateinit var userName: String
    lateinit var userField: String
    lateinit var userIntro: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_join)
        initJoin()
    }

    private fun initJoin() {
        joinBtn.setOnClickListener {
            userName = binding.nameInputEdit.text.toString()
            userField = binding.majorTagInputEdit.text.toString()
            userIntro = binding.userIntroduce.text.toString()

            val userData = hashMapOf(
                "email" to userInfoEmail,
                "field" to userField,
                "introduction" to userIntro,
                "name" to userName,
                "rating" to null,
            )

            val db = Firebase.firestore
            db.collection("User").document(userInfoEmail)
                .set(userData)
                .addOnSuccessListener { Log.d("로그인", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            var intent = Intent(applicationContext, SearchTeamActivity::class.java)
            startActivity(intent)
        }
    }
}