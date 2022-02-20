package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*



data class Case(
    val detail: String="",
    val date: String="",
    val uid: String="",
    val acctype: String="",
    val injured: String=""
)
class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NewsActivity : AppCompatActivity(){


    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportActionBar!!.title = "Traffic news"

        db = FirebaseFirestore.getInstance()
        val query = db.collection("Report").orderBy("date", Query.Direction.DESCENDING)
        val options = FirestoreRecyclerOptions.Builder<Case>().setQuery(query, Case::class.java).setLifecycleOwner(this).build()
        val adapter = object: FirestoreRecyclerAdapter<Case, UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(this@NewsActivity).inflate(R.layout.example, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: Case) {
                val detailTest:TextView = holder.itemView.findViewById(R.id.text_view_1)
                val dateTest: TextView = holder.itemView.findViewById(R.id.text_view_2)
                detailTest.text = model.detail
                dateTest.text = model.date
            }

        }

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        //recycler_view.setHasFixedSize(true)

    }





}