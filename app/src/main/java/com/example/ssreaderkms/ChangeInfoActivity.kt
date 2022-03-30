package com.example.ssreaderkms

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.ssreaderkms.SplashActivity.Companion.accountUserLogin
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class ChangeInfoActivity : AppCompatActivity() {
    lateinit var accountAvatarToEdit: ImageView
    var imageUri : Uri = Uri.EMPTY //this Uri to upload to storage

    lateinit var alertDialog :android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)

        accountAvatarToEdit = findViewById(R.id.accountAvatarToEdit)
        val EditFullnameEdt = findViewById<EditText>(R.id.EditFullnameEdt)
        val backPrev = findViewById<ImageView>(R.id.backToPrevEditInfo)
        val editBtn = findViewById<Button>(R.id.editInfobtn)

        alertDialog = SpotsDialog.Builder().setContext(this)
            .setCancelable(false)
            .setMessage("Editing")
            .build()

        EditFullnameEdt.text = Editable.Factory.getInstance().newEditable(accountUserLogin!!.fullname)

        if(!accountUserLogin!!.avatarUrl.equals(""))
        {

            Picasso.get().load(accountUserLogin!!.avatarUrl)
                .error(R.drawable.ic_profile).into(accountAvatarToEdit)
        }
        else
        {
            accountAvatarToEdit.setImageResource(R.drawable.ic_profile)
        }

        accountAvatarToEdit.setOnClickListener {
            val intentPickImg =Intent(Intent.ACTION_PICK)
            intentPickImg.type = "image/*"
            startActivityForResult(intentPickImg, 1000)
        }

        editBtn.setOnClickListener {
            val currentUser = Firebase.auth.currentUser
            accountUserLogin!!.fullname = EditFullnameEdt.text.toString()
            alertDialog.show()

            if (imageUri != Uri.EMPTY) // change avatar
            {
                if (!accountUserLogin!!.avatarUrl.equals(""))
                {
                                deleteImgInStorage()
                }
                uploadImgToStorage()
            }
            else //no changing avatar
            {
                updateInfoInDatabase()
            }
        }

        backPrev.setOnClickListener {
            finish()
        }
    }

    private fun uploadImgToStorage() {
        val path ="Accounts/${FirebaseAuth.getInstance().currentUser!!.uid}/"
        val storageRef = FirebaseStorage.getInstance().getReference(path + "Avatar")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                var UriImageTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!UriImageTask.isSuccessful);
                val imgURL : String = "${UriImageTask.result}"
                accountUserLogin?.avatarUrl = imgURL
                updateInfoInDatabase()
            }
    }

    private fun updateInfoInDatabase() {
        val accountRef = Firebase.database.getReference("Account")
        accountRef.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(accountUserLogin)
            .addOnSuccessListener {
                Toast.makeText(this,"Change Successfully", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
            .addOnFailureListener{e ->
                //fail to add in database
                Log.i("Error Register", e.message.toString())
            }
    }

    private fun deleteImgInStorage() {
        val delRef = FirebaseStorage.getInstance().getReferenceFromUrl(accountUserLogin?.avatarUrl!!)
        delRef.delete()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            accountAvatarToEdit!!.setImageURI(data?.data)
            imageUri = data?.data!!
        }
    }
}