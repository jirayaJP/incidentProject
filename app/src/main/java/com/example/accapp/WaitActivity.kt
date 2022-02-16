package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WaitActivity : AppCompatActivity() {

    lateinit var signoutB: Button
    lateinit var nexttomap: Button
    lateinit var helper_name: TextView
    lateinit var email_help: TextView
    lateinit var helperView: ImageView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait)
        signoutB = findViewById(R.id.signoutB)
        nexttomap = findViewById(R.id.nexttomap)
        helper_name = findViewById(R.id.helper_name)
        email_help = findViewById(R.id.Email_help)
        helperView = findViewById(R.id.helperView)
        db = FirebaseFirestore.getInstance()

        mAuth = FirebaseAuth.getInstance()
        nexttomap.setOnClickListener{
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        signoutB.setOnClickListener{
            mAuth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        val user = mAuth.currentUser
        helper_name.text = user?.displayName
        email_help.text = user?.email


        Glide.with(this).load(user?.photoUrl).into(helperView);
    }


}