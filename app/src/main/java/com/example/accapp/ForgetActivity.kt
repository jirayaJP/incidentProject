package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgetActivity : AppCompatActivity() {

    lateinit var txtEmailForget: EditText
    lateinit var ForgetPass: Button

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        mAuth = FirebaseAuth.getInstance()

        txtEmailForget = findViewById(R.id.txtEmailForget)
        ForgetPass = findViewById(R.id.ForgetPass)

        ForgetPass!!.setOnClickListener{
            val email = txtEmailForget.text.toString()
            if(TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please enter your Email!", Toast.LENGTH_SHORT).show()
            }else{
                mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener {
                        task -> if(task.isSuccessful) {
                            Toast.makeText(this,"Please check your Email", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,"Fail to send password Email", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}