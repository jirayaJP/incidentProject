package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    lateinit var logoutB : Button
    private lateinit var mAuth: FirebaseAuth
    lateinit var name_txt:TextView
    lateinit var email_txt:TextView
    lateinit var profileView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar!!.title = "User Profile"
        name_txt=findViewById(R.id.name_txt)
        email_txt=findViewById(R.id.email_txt)
        profileView=findViewById(R.id.profileView)

        mAuth = FirebaseAuth.getInstance()
        logoutB = findViewById(R.id.logoutB)
        logoutB.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val user = mAuth.currentUser

        name_txt.text = user?.displayName
        email_txt.text = user?.email

        Glide.with(this).load(user?.photoUrl).into(profileView);
    }
}