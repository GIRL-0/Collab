package com.example.collab

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collab.UserInfo.userInfoEmail
import com.example.collab.UserInfo.userInfoName
import com.example.collab.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var context = this
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        //TODO: 자동로그인
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInGoogleBtn: SignInButton = findViewById(R.id.googleLoginBtn)
        setGoogleButtonText(signInGoogleBtn, "Google 계정으로 로그인")

        signInGoogleBtn.setOnClickListener {
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 9001)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val email = auth.currentUser?.email
                    val name = auth.currentUser?.displayName
                    val photoUrl = auth.currentUser?.photoUrl
                    userInfoEmail = email!!
                    userInfoName = name!!

                    //db 연동

                    val db = Firebase.firestore
                    //////////////////////////////////
                    val docRef = db.collection("User").document(email!!)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                Log.d(TAG, "User data found: ${document.data}")
                            } else {
                                Log.d(TAG, "No document data, loading joinActivity")
                                var intent = Intent(applicationContext, JoinActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get failed with ", exception)
                        }
                    /////////////////////////////

                    Toast.makeText(
                        applicationContext,
                        "Login Success with ${email.toString()} ${name.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        applicationContext, "${name.toString()}님 환영합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("googleLogin", user.toString())

                    var intent = Intent(context, SearchTeamActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                } else {
                    Log.d("googleLogin", "signInWithCredential: failure", task.exception)
                }
            }
    }

    private fun setGoogleButtonText(loginButton: SignInButton, buttonText: String){
        var i = 0
        while (i < loginButton.childCount){
            var v = loginButton.getChildAt(i)
            if (v is TextView) {
                var tv = v
                tv.text = buttonText
                tv.gravity = Gravity.CENTER
                tv.textSize = 14.0f
                return
            }
            i++
        }
    }
}