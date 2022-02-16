package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    lateinit var txtEmailcreate: EditText
    lateinit var txtPasswordcreate: EditText
    lateinit var txtName: EditText
    lateinit var txtWork: EditText
    lateinit var regisSubmit: Button
    lateinit var email:String
    lateinit var password:String
    lateinit var name:String
    lateinit var workplace:String

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtEmailcreate = findViewById(R.id.txtEmailcreate)
        txtPasswordcreate = findViewById(R.id.txtPasswordcreate)
        regisSubmit = findViewById(R.id.regisSubmit)
        txtName = findViewById(R.id.txtName)
        txtWork = findViewById(R.id.txtWork)


        regisSubmit.setOnClickListener{


            createAccount()
        }
    }

    private fun createAccount() {
        mAuth = FirebaseAuth.getInstance()
        email = txtEmailcreate.text.toString()
        password = txtPasswordcreate.text.toString()
        name = txtName.text.toString()
        workplace = txtWork.text.toString()
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {
            task -> if(task.isSuccessful){
                val user = mAuth.currentUser
            val uid = user?.uid
            if (uid != null) {
                saveFirestore(name, workplace, uid)
            }

            updateui(user)

            }else{
            Toast.makeText(this,"Authentication Failed", Toast.LENGTH_SHORT).show()
            updateui(null)
            }
        }

    }

    private fun saveFirestore(name: String, workplace: String, uid:String) {
        db = FirebaseFirestore.getInstance()
        val helper:MutableMap<String,Any> = HashMap()
        helper["name"] = name
        helper["workplace"] = workplace
        helper["uid"] = uid


        db.collection("users").add(helper).addOnSuccessListener {
            Toast.makeText(this, "added success", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Toast.makeText(this, "added failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateui(user: FirebaseUser?) {
        if(user != null){
            val intent = Intent(this,NextActivity1::class.java)
            startActivity(intent)
        }
    }


}