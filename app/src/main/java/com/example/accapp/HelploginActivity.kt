package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HelploginActivity : AppCompatActivity() {

    lateinit var submitButton: Button
    lateinit var txtEmail: EditText
    lateinit var txtPassword: EditText
    lateinit var regisB: Button
    lateinit var forgetButton: Button

    lateinit var email:String
    lateinit var password:String

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helplogin)
        supportActionBar!!.title = "Helper Login"

        submitButton = findViewById<Button>(R.id.submitB)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        regisB = findViewById(R.id.regisB)
        forgetButton = findViewById(R.id.forgetButton)

        submitButton!!.setOnClickListener{
            login()
        }

        regisB!!.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        forgetButton!!.setOnClickListener{
            val intent = Intent(this,ForgetActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        mAuth = FirebaseAuth.getInstance()
        super.onStart()
        val currentUser = mAuth!!.currentUser
        updateui(currentUser)
    }

    private fun login(){

        mAuth = FirebaseAuth.getInstance()
        email = txtEmail!!.text.toString()
        password = txtPassword!!.text.toString()
        mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {
                task -> if(task.isSuccessful){
            val user = mAuth!!.currentUser
            updateui(user)
        }else{
            Toast.makeText(this,"Authentication Failed", Toast.LENGTH_SHORT).show()
            updateui(null)
        }
        }
    }

    private fun updateui(user: FirebaseUser?) {
        if(user != null){
            val uid = user.uid
            val emailU = user.email
            Toast.makeText(this,"Welcome: $emailU your id is: $uid", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,WaitActivity::class.java)
            startActivity(intent)
        }
    }
}