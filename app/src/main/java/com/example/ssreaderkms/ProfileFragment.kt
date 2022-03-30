package com.example.ssreaderkms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ssreaderkms.SplashActivity.Companion.accountUserLogin
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    lateinit var accountAvatar : ImageView
    lateinit var nameAccount : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val accountInfoRow = view.findViewById<LinearLayout>(R.id.accountInfoRow)
        val favoriteRow = view.findViewById<LinearLayout>(R.id.favoriteRow)
        val logoutRows = view.findViewById<LinearLayout>(R.id.logoutRows)
        accountAvatar = view.findViewById(R.id.accountAvatar)
        nameAccount = view.findViewById(R.id.nameAccount)
        nameAccount.visibility = View.VISIBLE


        accountInfoRow.setOnClickListener {
            val intent = Intent(activity, ChangeInfoActivity::class.java)
            startActivity(intent)
        }

        favoriteRow.setOnClickListener {

        }

        logoutRows.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity,LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        nameAccount.text = accountUserLogin!!.fullname

        if(!accountUserLogin!!.avatarUrl.equals(""))
        {

            Picasso.get().load(accountUserLogin!!.avatarUrl)
                .error(R.drawable.ic_profile).into(accountAvatar)
        }
        else
        {
            accountAvatar.setImageResource(R.drawable.ic_profile)
        }

    }
    companion object {
        fun newInstance(){

        }
    }
}