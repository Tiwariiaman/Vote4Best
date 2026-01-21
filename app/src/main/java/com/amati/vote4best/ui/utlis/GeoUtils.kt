package com.amati.vote4best.ui.utlis

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getCityName(
    context: Context,
    latitude: Double,
    longitude: Double
): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(latitude, longitude, 1)

        addresses?.firstOrNull()?.locality ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}