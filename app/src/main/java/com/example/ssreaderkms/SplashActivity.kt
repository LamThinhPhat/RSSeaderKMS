package com.example.ssreaderkms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                nextActivity()
            }

        }, 2000)
    }

    private fun nextActivity() {
        val user : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        if (user == null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
        }
        else{
            val intent = Intent(this, ContentActivity::class.java)
            startActivity(intent);
        }
    }
}