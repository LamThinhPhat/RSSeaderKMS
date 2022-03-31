package com.example.ssreaderkms.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.ssreaderkms.Models.AccountUser
import com.example.ssreaderkms.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    companion object{
        var accountUserLogin : AccountUser? = null
    }

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
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
        }
        else{
            val userFromFireAuth = FirebaseAuth.getInstance().currentUser

            val accountRef = FirebaseDatabase.getInstance().getReference("Account/${userFromFireAuth!!.uid}")


            accountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    accountUserLogin = snapshot.getValue(AccountUser::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SplashActivity,"" + error.message, Toast.LENGTH_SHORT).show()
                }
            })
            val intent = Intent(this, ContentActivity::class.java)
            startActivity(intent);
        }
    }
}