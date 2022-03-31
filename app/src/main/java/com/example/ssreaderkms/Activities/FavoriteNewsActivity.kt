package com.example.ssreaderkms.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssreaderkms.Adapters.MyNewsAdapter
import com.example.ssreaderkms.Activities.SplashActivity.Companion.accountUserLogin
import com.example.ssreaderkms.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavoriteNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorie_news)

        val backIV = findViewById<ImageView>(R.id.backToPrevFavorite)
        val favoriteNewsRV = findViewById<RecyclerView>(R.id.FavotireNewsRV)


        val favoriteAdapter = MyNewsAdapter(accountUserLogin!!.markList!!,this)

        favoriteNewsRV!!.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )

        favoriteNewsRV.adapter = favoriteAdapter
        favoriteNewsRV.layoutManager = LinearLayoutManager(this)

        favoriteAdapter.onMarkClick = { newsRows, b ->
            val tempPosition = accountUserLogin!!.markList.indexOf(newsRows)
            accountUserLogin!!.markList.remove(newsRows)
            favoriteAdapter.notifyItemRemoved(tempPosition)
            updateMarkListInDB()
        }

        favoriteAdapter.onClickNews = {linkUrl ->
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("UrlPage", linkUrl)
            startActivity(intent)
        }
        backIV.setOnClickListener {
            finish()
        }
    }

    private fun updateMarkListInDB() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val accountRef = FirebaseDatabase.getInstance().getReference("Account/${uid}")
        accountRef.setValue(accountUserLogin)
    }
}