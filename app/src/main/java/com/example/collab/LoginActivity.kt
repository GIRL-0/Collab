package com.example.collab

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.collab.databinding.ActivityLoginBinding
import com.example.collab.databinding.ActivityPersonalCalendarBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var context = this

    private lateinit var client: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initLogin()
    }

    private fun initLogin() {
        setGoogleLogin()
    }

    fun setGoogleLogin(){
        // 요청 정보 옵션
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        client = GoogleSignIn.getClient(this, options)

        login_button.setOnClickListener {
            // 로그인 요청
            startActivityForResult(client.signInIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            var account: GoogleSignInAccount? = null
            try {
                account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Failed Google Login $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // 인증에 성공한 후, 현재 로그인된 유저의 정보를 가져올 수 있습니다.
                        val email = auth.currentUser?.email
                        val name = auth.currentUser?.displayName
                        val photoUrl = auth.currentUser?.photoUrl
                    }
                })
    }





}