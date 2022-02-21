package com.example.accapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class NewsDetailActivity : AppCompatActivity() {
    lateinit var inputInjure: TextView
    lateinit var inputDead: TextView
    lateinit var inputDate: TextView
    lateinit var heading: TextView
    lateinit var detailNews: TextView
    lateinit var numofInjured: String

    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var firebaseStorage: FirebaseStorage
    companion object{
        private const val RC_SIGN_IN = 100
        const val TAG = "TEST_TAG"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        heading = findViewById(R.id.heading)

        detailNews = findViewById(R.id.detailNews)
        inputInjure = findViewById(R.id.inputInjure)
        inputDead = findViewById(R.id.inputDead)
        inputDate = findViewById(R.id.inputDate)


        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        db.collection("Report").get().addOnSuccessListener{ result ->
            for (document in result) {
                //Log.d(TAG, "${document.id} => ${document.data}")


            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


        }

}