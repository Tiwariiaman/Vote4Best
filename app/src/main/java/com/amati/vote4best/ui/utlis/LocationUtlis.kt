package com.amati.vote4best.ui.utlis

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onLocation: (Double, Double) -> Unit,
    onError: () -> Unit
) {
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                onLocation(location.latitude, location.longitude)
            } else {
                onError()
            }
        }
        .addOnFailureListener {
            onError()
        }
}