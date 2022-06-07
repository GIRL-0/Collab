package com.example.collab

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.databinding.ActivityProfileBinding
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_team_search.*
import java.io.InputStream
import java.util.*


class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var profileNoticeRecyclerView: RecyclerView
    val profileNoticeData: ArrayList<ProfileNoticeData> = ArrayList()
    var context = this
    lateinit var adapter: ProfileNoticeAdapter
    var storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
        initProfile()
        initProfileNoticeData()
        initRecyclerView()

        Intent(this@ProfileActivity, CreateTeamActivity::class.java).apply {
            putExtra("email", binding.userName.text.toString())
        }


    }

    private val GALLERY = 1
    private fun initProfile() {
        var textUserName = binding.userName.text.toString()
        var textUserMajorTag = binding.userMajorTag.text.toString()
        var textUserIntroduce = binding.userIntroduce.text.toString()
        //변경사항 저장
        binding.saveChangesBtn.setOnClickListener {
            val userData = hashMapOf(
                "email" to userInfoEmail,
                "field" to binding.userMajorTag.text.toString(),
                "introduction" to binding.userIntroduce.text.toString(),
                "name" to binding.userName.text.toString(),
                "rating" to null,
            )

            val db = Firebase.firestore
            db.collection("User").document(userInfoEmail!!)
                .set(userData)
                .addOnSuccessListener { Log.d("테스트", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            Toast.makeText(applicationContext, "변경 사항이 저장되었습니다", Toast.LENGTH_SHORT).show()
            Log.d("테스트", "$test")
        }


        //db에서 불러오기 xml init
        val db = Firebase.firestore
        val docRef = db.collection("User").document(userInfoEmail!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.userMajorTag.setText(document.get("field").toString())
                    binding.userIntroduce.setText(document.get("introduction").toString())
                    binding.userName.setText(document.get("name").toString())
                    binding.userGradeNum.setText(document.get("rating").toString())
                    var profilePath = document.get("profilePic").toString()

                    //TODO : 프로필 사진 작업

                    Firebase.storage.reference.child("images/$userInfoEmail/$profilePath").downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Glide.with(context).load(it.result).into(binding.userImg)
                        }
                    }

                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        var changePfp = binding.changePfpBtn

        changePfp.setOnClickListener {
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
                var imageData: Uri? = data?.data
                val storageRef = storage.reference
                val profileRef =
                    storageRef.child("images/$userInfoEmail/${imageData?.lastPathSegment}")
                val profilePathName = imageData?.lastPathSegment.toString()
                val db = Firebase.firestore
                val data = hashMapOf("profilePic" to profilePathName)
                db.collection("User").document(userInfoEmail)
                    .set(data, SetOptions.merge())
                val uploadTask = profileRef.putFile(imageData!!)

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener {
                    Log.d("테스트", "실패")
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                    // ...
                }
                Toast.makeText(this, imageData.toString(), Toast.LENGTH_SHORT).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
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
