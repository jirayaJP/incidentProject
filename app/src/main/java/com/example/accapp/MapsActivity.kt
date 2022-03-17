package com.example.accapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.GnssAntennaInfo
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.accapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import android.view.inputmethod.EditorInfo

import android.widget.TextView.OnEditorActionListener
import java.io.IOException

import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import com.google.android.gms.location.LocationRequest
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private lateinit var mMap: GoogleMap
    lateinit var inputBar: EditText
    lateinit var reportMap: Button
    lateinit var mGps: ImageView
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var locateArray: ArrayList<Any>
    private var markerTest: Marker?=null
    private var markerTest2: Marker?=null
    private var markerTest3: Marker?=null
    private var markerTest4: Marker?=null

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101
    private val mLocationPermissionsGranted = false

    private lateinit var lastLocation: Location
    companion object {
        const val LOCATION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15f
        const val TAG = "TEST_TAG"
    }
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback:LocationCallback


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        inputBar = findViewById(R.id.inputBar)
        mGps = findViewById(R.id.mylocate)
        reportMap = findViewById(R.id.reportMap)

        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        locateArray=ArrayList()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        reportMap.setOnClickListener{
            val intent = Intent(this,reportActivity::class.java)
            startActivity(intent)
        }

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        db.collection("Report").whereEqualTo("acctype", "รถชน").get().addOnSuccessListener{ result ->
            for (document in result) {
                val locate = document.data.getValue("Location")




            }

        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        createNotificationChannel()
        setUpMap()


    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude,location.longitude)
                val newLatLng = LatLng(13.763683, 100.548529)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                val df = DecimalFormat("#.###")
                val distance = SphericalUtil.computeDistanceBetween(currentLatLong,newLatLng)
                val distanceB = distance/1000
                val roundoff = df.format(distanceB)
                if(distanceB < 0.5){
                    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Incident near here")
                        .setContentText("$roundoff"+" km " + " ห่างจากจุดเสี่ยงอันตราย โปรดขับรถอย่างระมัดระวัง")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    with(NotificationManagerCompat.from(this)){
                        notify(notificationID,builder.build())
                    }
                }


            }
        }


    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        val newLatLng = LatLng(13.748957, 100.563425)
        val newLatLng2 = LatLng(13.730016, 100.570331)
        val newLatLng3 = LatLng(13.797378, 100.627192)
        val newLatLng4 = LatLng(13.763683, 100.548529)
        markerOptions.title("current position")
        mMap.addMarker(markerOptions)
        markerTest = mMap.addMarker(markerOptions.position(newLatLng).title("จุดเสี่ยงอันตราย").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
        markerTest2 = mMap.addMarker(markerOptions.position(newLatLng2).title("จุดเสี่ยงอันตราย").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
        markerTest3 = mMap.addMarker(markerOptions.position(newLatLng3).title("จุดเสี่ยงอันตราย").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
        markerTest4 = mMap.addMarker(markerOptions.position(newLatLng4).title("จุดเสี่ยงอันตราย").icon(
            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
    }



    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        if (title != "My Location") {
            val options = MarkerOptions()
                .position(latLng)
                .title(title)
            mMap.addMarker(options)
        }
        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }






    override fun onMarkerClick(p0: Marker) = false

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

}