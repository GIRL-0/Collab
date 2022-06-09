package com.example.collab

import PersonalCalendarAdapter
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.databinding.ActivityProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_team_search.*
import java.util.*


@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    var context = this
    lateinit var notifRecyclerView: RecyclerView
    private val profileNoticeData: ArrayList<ProfileNoticeData> = ArrayList()
    var adapter: ProfileNoticeAdapter = ProfileNoticeAdapter(profileNoticeData)
    var storage = FirebaseStorage.getInstance()
    private val starArray = arrayOf("☆☆☆☆☆", "★☆☆☆☆", "★★☆☆☆", "★★★☆☆", "★★★★☆", "★★★★★")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initlayout()
        initProfile()
        initRecyclerView()

        Intent(this@ProfileActivity, CreateTeamActivity::class.java).apply {
            putExtra("email", binding.userName.text.toString())
        }

        binding.logoutBtn.setOnClickListener {
            firebaseAuthSignOut()
            Toast.makeText(applicationContext, "로그아웃되었습니다. 다시 로그인해주세요", Toast.LENGTH_SHORT).show()
            var intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

    }

    private val GALLERY = 1
    private fun initProfile() {
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
                .set(userData, SetOptions.merge())
                .addOnSuccessListener { Log.d("테스트", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

            Toast.makeText(applicationContext, "변경 사항이 저장되었습니다", Toast.LENGTH_SHORT).show()
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

                    if (document.get("rating") == null) {
                        binding.userGrade.visibility = LinearLayout.GONE
                    } else {
                        binding.userGradeNum.setText(document.get("rating").toString())
                        binding.userGradeStar.text =
                            starArray[document.get("rating").toString().toInt()]
                    }

                    var profilePath = document.get("profilePic").toString()
//                    프로필 사진
                    if (profilePath != "null") {
                        Firebase.storage.reference.child("images/$userInfoEmail/$profilePath")
                            .downloadUrl.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Glide.with(context)
                                        .load(it.result)
                                        .into(binding.userImg)
                                }
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

        val changePfp = binding.changePfpBtn

        changePfp.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY)
        }
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
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { taskSnapshot ->
                    // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                }
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                    binding.userImg.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun clearData() {
        profileNoticeData.clear() // clear list
        adapter.notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }

    private fun initRecyclerView() {
        val db = Firebase.firestore
        val docRef = db.collection("User").document(userInfoEmail)
        clearData()
        //get db
        docRef.get()
            .addOnSuccessListener { document ->
                if (document?.get("notifications") != null) {
                    var notifications = document.get("notifications") as ArrayList<String>
                    for (notification in notifications) {
                        profileNoticeData.add(
                            ProfileNoticeData(
                                notification
                            )
                        )
                    }
                    notifRecyclerView =
                        findViewById(R.id.profileNotifRecyclerView)
                    notifRecyclerView.layoutManager = LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = ProfileNoticeAdapter(profileNoticeData)
                    notifRecyclerView.adapter = adapter
                    adapter.itemClickListener = object : ProfileNoticeAdapter.OnItemClickListener {
                        override fun OnItemClick(data: ProfileNoticeData) {
                        }
                    }
                } else {
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
    }

    private fun firebaseAuthSignOut() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        Firebase.auth.signOut()
        googleSignInClient!!.signOut()
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
