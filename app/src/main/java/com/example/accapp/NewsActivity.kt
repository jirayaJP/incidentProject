package com.example.accapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


data class Case(
    val detail: String="",
    val date: String="",
    val uid: String="",
    val acctype: String="",
    val injured: String="",
    val dead: String=""
)
class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class NewsActivity : AppCompatActivity(){


    private lateinit var db: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var date:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportActionBar!!.title = "Traffic news"

        db = FirebaseFirestore.getInstance()

        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: String = simpleDateFormat.format(calendar.time)
        firebaseStorage = FirebaseStorage.getInstance()
        val query = db.collection("Report").whereEqualTo("date",date )
        val options = FirestoreRecyclerOptions.Builder<Case>().setQuery(query, Case::class.java).setLifecycleOwner(this).build()
        val adapter = object: FirestoreRecyclerAdapter<Case, UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(this@NewsActivity).inflate(R.layout.example, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: Case) {
                val detailTest:TextView = holder.itemView.findViewById(R.id.text_view_1)
                val dateTest: TextView = holder.itemView.findViewById(R.id.text_view_2)
                val injuredTest: TextView = holder.itemView.findViewById(R.id.text_view_3)
                val deadTest: TextView = holder.itemView.findViewById(R.id.text_view_4)
                val accTest: TextView = holder.itemView.findViewById(R.id.text_view_5)
                val imgTest: ImageView = holder.itemView.findViewById(R.id.image_news)
                detailTest.text = model.detail
                dateTest.text = model.date
                injuredTest.text = model.injured
                deadTest.text = model.dead
                accTest.text = model.acctype
                val ref = firebaseStorage.reference.child("test/image/test")
                val localFile = File.createTempFile("detail","jpg")
                ref.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    imgTest.setImageBitmap(bitmap)
                }.addOnFailureListener{
                    Toast.makeText(applicationContext, "fail" , Toast.LENGTH_SHORT).show()
                }
            }

        }

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }



}