package com.example.accapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

class NextActivity1 : AppCompatActivity() {


    var maps: CardView? = null
    var report: CardView? = null
    var news: CardView ?= null
    var profile: CardView ?= null
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next1)

        maps = findViewById(R.id.maps)
        report = findViewById(R.id.report)
        news = findViewById(R.id.News)
        profile = findViewById(R.id.profile)

        createNotificationChannel()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }

//        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
//            if (location != null) {
//                lastLocation = location
//                val currentLatLong = LatLng(location.latitude, location.longitude)
//                val newLatLng = LatLng(13.763774, 100.548492)
//                val distance = SphericalUtil.computeDistanceBetween(currentLatLong, newLatLng)
//                val df = DecimalFormat("#.###")
//                val distanceB = (distance / 1000)
//                val roundoff = df.format(distanceB)
//                if (distanceB < 0.5) {
//                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_launcher_foreground)
//                        .setContentTitle("Incident near here")
//                        .setContentText("$roundoff"+"m" + "ห่างจากจุดเสี่ยงอันตราย โปรดขับรถอย่างระมัดระวัง")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    with(NotificationManagerCompat.from(this)) {
//                        notify(notificationID, builder.build())
//                    }
//                }
//            }
//        }


        report!!.setOnClickListener{
            val intent = Intent(this,reportActivity::class.java)
            startActivity(intent)
        }

        news!!.setOnClickListener{
            val intent = Intent(this,NewsActivity::class.java)
            startActivity(intent)
        }

        maps!!.setOnClickListener{
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

        profile!!.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun createNotificationChannel(){
        val name = "Incident Notification"
        val descriptionText = "Notification Description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
            description=descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}