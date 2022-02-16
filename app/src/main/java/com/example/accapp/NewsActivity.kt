package com.example.accapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class NewsActivity : AppCompatActivity(), ExampleAdapter.OnItemClickListener{



    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    //val exampleList= generateDummyList(500)
    //private val adapter = ExampleAdapter(exampleList as ArrayList<Report>, this)
    private val reportList = ArrayList<Report>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportActionBar!!.title = "Traffic news"
        var currentUser = auth.currentUser


        //val recycler_view : RecyclerView = findViewById(R.id.recycler_view)
        //recycler_view.adapter = adapter
        //recycler_view.layoutManager = LinearLayoutManager(this)
        //recycler_view.setHasFixedSize(true)
        loadAllData()

    }

//    private fun generateDummyList(size: Int): List<Report> {
//
//        val list = ArrayList<Report>()
//
//        for (i in 0 until size) {
//            val drawable = when (i % 3) {
//                0 -> R.drawable.ic_baseline_android_24
//                1 -> R.drawable.ic_baseline_apartment
//                else -> R.drawable.ic_baseline_directions_car
//            }
//
//            val item = Report(drawable, "Item $i", "Line 2")
//            list += item
//        }
//
//        return list
//    }




    override fun onItemClick(position: Int) {
        Toast.makeText(this, "news $position clicked", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@NewsActivity,NewsDetailActivity::class.java)
        intent.putExtra("heading",reportList[position].date)

        intent.putExtra("detailNews",reportList[position].detail)

        startActivity(intent)
    }

    private fun loadAllData(){

        val reportList = ArrayList<Report>()
        val ref = db.collection("Report")
        val recycler_view : RecyclerView = findViewById(R.id.recycler_view)
        ref.get().addOnSuccessListener {
            if (it.isEmpty){
                Toast.makeText(this,"not found",Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            for(doc in it){

                val reportModel = doc.toObject(Report::class.java)
                reportList.add(reportModel)
                recycler_view.adapter = ExampleAdapter(reportList,this)
                //adapter = ExampleAdapter(reportList,this)
                recycler_view.layoutManager = LinearLayoutManager(this)
                recycler_view.setHasFixedSize(true)
            }
        }
    }


}