package com.example.ssreaderkms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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
                progressBar.visibility = View.GONE
                startActivity(Intent(this, ContentActivity::class.java))
            }
            ?.addOnFailureListener{
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Wrong EMail or Password", Toast.LENGTH_SHORT).show()
            }
    }
}