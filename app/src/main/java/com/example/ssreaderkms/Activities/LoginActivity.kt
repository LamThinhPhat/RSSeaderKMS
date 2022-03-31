package com.example.ssreaderkms.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.ssreaderkms.Models.AccountUser
import com.example.ssreaderkms.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        emailEditText = findViewById(R.id.LoginMailedt)
        passwordEditText = findViewById(R.id.LoginPassedt)
        loginBtn = findViewById(R.id.LoginBtn)
        registerBtn = findViewById(R.id.RegisterBtn)
        progressBar = findViewById(R.id.progressBarLogin)

        mAuth = FirebaseAuth.getInstance()

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            loginAccount()
        }

    }

    private fun loginAccount() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty())
        {
            emailEditText.setError("Required")
            emailEditText.requestFocus()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Required valid email")
            emailEditText.requestFocus()
        }

        if (password.isEmpty())
        {
            passwordEditText.setError("Required")
            passwordEditText.requestFocus()
        }

        progressBar.visibility = View.VISIBLE

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnSuccessListener {
                val userFromFireAuth = FirebaseAuth.getInstance().currentUser

                val accountRef = FirebaseDatabase.getInstance().getReference("Account/${userFromFireAuth!!.uid}")


                accountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        SplashActivity.accountUserLogin = snapshot.getValue(AccountUser::class.java)!!
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity,"" + error.message, Toast.LENGTH_SHORT).show()
                    }
                })
                progressBar.visibility = View.GONE
                startActivity(Intent(this, ContentActivity::class.java))
            }
            ?.addOnFailureListener{
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Wrong EMail or Password", Toast.LENGTH_SHORT).show()
            }
    }
}