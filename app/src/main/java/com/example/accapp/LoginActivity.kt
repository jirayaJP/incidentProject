package com.example.accapp

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Base64.encode
import android.util.Log
import android.widget.Button
import com.google.android.gms.common.util.Base64Utils.encode
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.qrcode.encoder.Encoder.encode
import java.lang.Exception
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

open class LoginActivity : AppCompatActivity() {

    var google_button: Button? = null
    var facebook_button: Button? = null

    private lateinit var firebaseAuth : FirebaseAuth

    companion object{
        private const val RC_SIGN_IN = 100
        const val TAG = "SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "User Login"

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