package com.example.collab

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
            userName = nameInputEdit.text.toString()
            userField = majorTagInputEdit.text.toString()
            userIntro = userIntroduce.text.toString()

            val userData = hashMapOf(
                "email" to userInfoEmail,
                "field" to userField,
                "introduction" to userIntro,
                "name" to userName,
                "rating" to "0",
                "plans" to null,
                "notifications" to null,
                "profilePic" to null,
            )

            val db = Firebase.firestore
            db.collection("User").document(userInfoEmail)
                .set(userData)
                .addOnSuccessListener { Log.d("로그인", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { exception -> Log.w(TAG, "Error writing document", exception) }

            Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()

            var intent = Intent(applicationContext, SearchTeamActivity::class.java)
            startActivity(intent)
        }
    }
}