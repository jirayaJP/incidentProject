package com.example.accapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.provider.MediaStore
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class reportActivity : AppCompatActivity() {
    private var our_request_code : Int = 123
    private var video_request_code : Int = 111
    private var image_request_code : Int = 100
    private lateinit var videoView : VideoView
    lateinit var filepath : Uri
    lateinit var videoUri : Uri
    lateinit var submit_button: Button
    lateinit var Describedetail : EditText
    lateinit var radiogroup_type: RadioGroup

    lateinit var radioButton: RadioButton
    lateinit var injuredPatient: EditText
    lateinit var deadPatient: EditText
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var date:String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var firebaseStorage: FirebaseStorage
    private lateinit var db: FirebaseFirestore

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        supportActionBar!!.title = "Accident Report"
        videoView = findViewById(R.id.videoView)
        val mediacollect = MediaController(this)
        mediacollect.setAnchorView(videoView)
        videoView.setMediaController(mediacollect)
        Describedetail = findViewById(R.id.DescribeDetail)
        injuredPatient = findViewById(R.id.injured)
        deadPatient = findViewById(R.id.dead)

        radiogroup_type = findViewById(R.id.radiogroup)
        createNotificationChannel()


        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        firebaseStorage = FirebaseStorage.getInstance()

        submit_button = findViewById(R.id.submitB)
        submit_button.setOnClickListener(){
            submitPic()
            finish()
        }
    }


    private fun saveFirestore(detail: String, uid:String, acctype: String,injured: String,dead: String, date: String, currentLatLng: LatLng) {


        db = FirebaseFirestore.getInstance()
        val user:MutableMap<String,Any> = HashMap()
        user["detail"] = detail
        user["uid"] = uid
        user["acctype"] = acctype
        user["injured"] = injured
        user["dead"] = dead
        user["date"] = date
        user["Location"] = currentLatLng

        db.collection("Report").add(user).addOnSuccessListener {
            Toast.makeText(this, "added success", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Toast.makeText(this, "added failed", Toast.LENGTH_SHORT).show()
            }
    }




    @SuppressLint("SimpleDateFormat")
    fun submitPic(){
        if(filepath!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val fileName = formatter.format(now)
            val detail = Describedetail.text.toString()
            val radioId = radiogroup_type.checkedRadioButtonId
            val injured = injuredPatient.text.toString()
            val dead = deadPatient.text.toString()
            val user = mAuth.currentUser
            val uid = user?.uid


            radioButton = findViewById(radioId)

            val acctype = radioButton.text.toString()

            calendar = Calendar.getInstance()
            simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
            val date: String = simpleDateFormat.format(calendar.time)
            mAuth = FirebaseAuth.getInstance()
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



            var imageRef = FirebaseStorage.getInstance().reference.child("test/image/$detail")
            imageRef.putFile(filepath)
                .addOnSuccessListener {p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"file Uploaded",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{p0 ->
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }





            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
                return
            }

            fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null){
                    lastLocation = location
                    val currentLatLong = LatLng(location.latitude,location.longitude)

                    if (uid != null) {
                        saveFirestore(detail,uid, acctype,injured,dead, date ,currentLatLong)
                    }

                }
            }


        }

    }

    fun uploadvideo(){
        if(videoUri!=null){
            var pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()


            val timestamp = ""+System.currentTimeMillis()
            val videopath = "test/videos/video_$timestamp"
            val videoref = FirebaseStorage.getInstance().reference.child(videopath)

            videoref.putFile(videoUri)
                .addOnSuccessListener {p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"file Uploaded",Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{p0 ->
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()
                }
        }
    }


    fun record(view: View){
        val intent  = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,video_request_code)
        }
    }

    fun takephoto(view: View){

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,our_request_code)
        }
    }

    fun pickimage(view: View){

        val intent = Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        intent.setType( "image/*" );
        if(intent.resolveActivity(packageManager)!=null){
            startActivityForResult(intent,image_request_code)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == our_request_code && resultCode == RESULT_OK){

            val imageView : ImageView = findViewById(R.id.image)
            var bitmap = data?.extras?.get("data") as Bitmap

            imageView.setImageBitmap(bitmap)


        }
        if(requestCode == image_request_code && resultCode == RESULT_OK){
            filepath = data?.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            val imageView : ImageView = findViewById(R.id.image)
            imageView.setImageBitmap(bitmap)


        }

        if(requestCode == video_request_code && resultCode == RESULT_OK){
            videoUri = data?.data!!
            videoView.setVideoURI(videoUri)
            videoView.start()

            submit_button.setOnClickListener(){
                uploadvideo()
                submitPic()
            }
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