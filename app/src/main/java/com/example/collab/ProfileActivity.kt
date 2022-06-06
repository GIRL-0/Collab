package com.example.collab

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.ActivityProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_team_search.*
import java.util.*

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var profileNoticeRecyclerView: RecyclerView
    val profileNoticeData: ArrayList<ProfileNoticeData> = ArrayList()
    var context = this
    lateinit var adapter: ProfileNoticeAdapter
    var firestore: FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
        initProfile()
        initProfileNoticeData()
        initRecyclerView()

    }

    private val GALLERY = 1
    private fun initProfile() {
        var textUserName = binding.userName.text.toString()
        var textUserMajorTag = binding.userMajorTag.text.toString()
        var textUserIntroduce = binding.userIntroduce.text.toString()

        //TODO: 데이터베이스에 저장하고 호출시마다 프로필 업데이트
        val userData = hashMapOf(
            "email" to email,
            "field" to null,
            "introduction" to null,
            "name" to name ,
            "rating" to null,
            "teams" to null,
        )
        val db = Firebase.firestore

        db.collection("User").document(email!!)
            .set(userData)
            .addOnSuccessListener { Log.d("테스트", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        Log.d("테스트", "$test")


        //TODO: 데이터베이스에 저장하고 호출시마다 프로필 업데이트

        var userProfileImg = binding.userImg

        userProfileImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)

        }
        Toast.makeText(applicationContext, textUserName, Toast.LENGTH_SHORT).show()

    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                var ImageData: Uri? = data?.data
                Toast.makeText(this, ImageData.toString(), Toast.LENGTH_SHORT).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImageData)
                    //TODO: 데이터베이스에 저장하고 다시 ImageView로 불러오기
                    binding.userImg.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initProfileNoticeData() {
        val scan = Scanner(resources.openRawResource(R.raw.words))
        while (scan.hasNextLine()) {
            //TODO: 데이터베이스 불러오기
            val val1 = scan.nextLine()
            val val2 = scan.nextLine()
            val val3 = scan.nextLine()
            profileNoticeData.add(ProfileNoticeData(val3))
        }
    }

    private fun initRecyclerView() {
        Toast.makeText(applicationContext, "initProfileRecyclerView()", Toast.LENGTH_SHORT).show()
        profileNoticeRecyclerView = findViewById(R.id.profileAlarmRecyclerView)
        profileNoticeRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        adapter = ProfileNoticeAdapter(profileNoticeData)
        profileNoticeRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ProfileNoticeAdapter.OnItemClickListener {
            override fun OnItemClick(data: ProfileNoticeData) {
                Toast.makeText(applicationContext, data.alarmContent, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initlayout() {
        binding.apply {
            teamSearchTabMenu.setOnClickListener {
                var intent = Intent(context, SearchTeamActivity::class.java)
                startActivity(intent)
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

            }
        }
    }
}
