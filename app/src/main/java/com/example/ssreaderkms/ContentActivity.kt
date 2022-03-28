package com.example.ssreaderkms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ssreaderkms.Models.AccountUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContentActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var accountRef : DatabaseReference
    lateinit var testingUser: TextView
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        bottomNav = findViewById(R.id.BottomNav)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNav.setupWithNavController(navController)
    }

    private fun getInfo() {
        mAuth = FirebaseAuth.getInstance()

        val currentAccountUID = mAuth!!.currentUser!!.uid
        Log.i("testing", currentAccountUID)

        accountRef =FirebaseDatabase.getInstance().getReference("Account")
        accountRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var temp : AccountUser? = null
                for (child in snapshot.children)
                {
                    if (child.key == currentAccountUID) {
                        temp = child.getValue(AccountUser::class.java)!!
                        break
                    }
                }
                testingUser.text = temp!!.email.toString() + temp.fullname.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("onCancelled", error.toException())
            }
        })
    }
}