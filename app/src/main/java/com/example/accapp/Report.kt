package com.example.accapp

import com.google.android.gms.maps.model.LatLng

data class Report(var date: String= "", var detail: String="", var acctype: String="", var dead: String="", var injured: String="", var uid: String="", var Location: LatLng)
