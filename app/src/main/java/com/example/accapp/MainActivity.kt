package com.example.accapp

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    var google_button: Button? = null
    var facebook_button: Button? = null

    private lateinit var firebaseAuth : FirebaseAuth

    companion object{
        private const val RC_SIGN_IN = 100
        const val TAG = "SIGN_IN_TAG"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        google_button = findViewById<Button>(R.id.google_button)
        facebook_button = findViewById(R.id.facebook_button)

        google_button!!.setOnClickListener{
            val intent = Intent(this,GoogleSigninActivity::class.java)
            startActivity(intent)
        }
        facebook_button!!.setOnClickListener{
            val intent = Intent(this,FacebookAuthActivity::class.java)

            startActivity(intent)
        }

    }


}