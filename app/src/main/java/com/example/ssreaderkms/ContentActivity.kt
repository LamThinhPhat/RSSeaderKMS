package com.example.ssreaderkms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.ssreaderkms.Models.AccountUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ContentActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    lateinit var accountRef : DatabaseReference
    lateinit var testingUser: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        testingUser = findViewById(R.id.TestingUser)
        getInfo()
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