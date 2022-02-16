package com.example.accapp

import android.Manifest
import android.content.ContentProviderClient
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.GnssAntennaInfo
import android.location.Location
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
import com.google.android.gms.location.LocationCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.SphericalUtil


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {

    private lateinit var mMap: GoogleMap
    lateinit var inputBar: EditText
    lateinit var reportMap: Button
    lateinit var mGps: ImageView


    private lateinit var db: FirebaseFirestore
    private val mLocationPermissionsGranted = false

    private lateinit var lastLocation: Location
    companion object {
        const val LOCATION_REQUEST_CODE = 1
        private const val DEFAULT_ZOOM = 15f
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


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
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
                val newLatLng = LatLng(13.8424882967, 100.567494397)
                //Toast.makeText(this, "$currentLatLong", Toast.LENGTH_SHORT).show()
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                //Location.distanceBetween(location.latitude,location.longitude,results)
                val distance = SphericalUtil.computeDistanceBetween(currentLatLong,newLatLng)
                val distanceB = distance/1000
                Toast.makeText(this, "$distanceB" , Toast.LENGTH_SHORT).show()


            }
        }


    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)

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

}