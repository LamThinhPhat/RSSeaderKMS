package com.example.ssreaderkms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.ssreaderkms.Models.AccountUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private lateinit var fullnameEditText : EditText
    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var confirmbtn: Button
    private lateinit var cancelbtn: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
        fullnameEditText = findViewById(R.id.RegisFullnameEdt)
        emailEditText = findViewById(R.id.RegisMailedt)
        passwordEditText = findViewById(R.id.RegisPassedt)
        progressBar = findViewById(R.id.progressBarRegis)
        confirmbtn = findViewById(R.id.ConfRegisterBtn)
        cancelbtn = findViewById(R.id.CancelBtn)

        confirmbtn.setOnClickListener {
            registerAccount()
        }

        cancelbtn.setOnClickListener {
            finish()
        }

    }

    private fun registerAccount() {
        val fullname = fullnameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (fullname.isEmpty())
        {
            fullnameEditText.setError("Required")
            fullnameEditText.requestFocus()
        }

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

        if (password.length < 8)
        {
            passwordEditText.setError("At least 8 characters")
            passwordEditText.requestFocus()
        }

        progressBar.visibility = View.VISIBLE

        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnSuccessListener {
                //after add in authentication, add info to RealTime Database
                saveInfo(fullname, email)
            }
            ?.addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Log.i("Error Register", e.message.toString())
            }

    }

    private fun saveInfo(fullname: String, email : String) {
        val temp = AccountUser()
        temp.fullname = fullname
        temp.email = email


        val db = Firebase.database
        val myRef = db.getReference("Account");

        myRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(temp)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                finish()

            }
            .addOnFailureListener{e ->
                //fail to add in database
                Log.i("Error Register", e.message.toString())
            }
    }
}