package com.example.collab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collab.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var context = this
    private var googleSignInClient: GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth

    //    private lateinit var client: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInGoogleBtn: SignInButton = findViewById(R.id.googleLoginBtn)

        signInGoogleBtn.setOnClickListener {
            val signInIntent = googleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 9001)
        }

//        val logoutBtn = findViewById<TextView>(R.id.titleTextView)
//        logoutBtn.setOnClickListener {
//            firebaseAuthSignOut()
//        }

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

    private fun firebaseAuthSignOut() {
        Firebase.auth.signOut()
        googleSignInClient!!.signOut()
    }

//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1004) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            var account: GoogleSignInAccount? = null
//            try {
//                account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account!!.idToken)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Failed Google Login $e", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String?) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this,
//                OnCompleteListener<AuthResult?> { task ->
//                    if (task.isSuccessful) {
//                        val email = auth.currentUser?.email
//                        val name = auth.currentUser?.displayName
//                        val photoUrl = auth.currentUser?.photoUrl
//                        Toast.makeText(applicationContext, "Login Success ${email.toString()} ${name.toString()} ${photoUrl.toString()}",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                    else{
//                        Toast.makeText(applicationContext, "Login Fail",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                })
//
//    }
//
//


}