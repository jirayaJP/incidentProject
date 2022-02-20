package com.example.accapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NextActivity1 : AppCompatActivity() {


    var maps: CardView? = null
    var report: CardView? = null
    var news: CardView ?= null
    var profile: CardView ?= null
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101
    lateinit var noti_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next1)

        maps = findViewById(R.id.maps)
        report = findViewById(R.id.report)
        news = findViewById(R.id.News)
        profile = findViewById(R.id.profile)

        createNotificationChannel()
        noti_button=findViewById(R.id.testnoti)
        noti_button.setOnClickListener(){
            sendNotification()

        }

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Incident near here")
            .setContentText("incident description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(notificationID,builder.build())
        }
    }
}