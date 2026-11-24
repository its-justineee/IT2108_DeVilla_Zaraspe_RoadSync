
package com.example.roadsync

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

class LocationTrackingManager(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,
    private val onLocationChanged: (Location) -> Unit,
    private val onCameraToLocation: (Location) -> Unit,
    private val onTrackingStarted: () -> Unit
) {

    private var isTracking = false
    private var latestLocation: Location? = null

    private val locationCallback =
        object : LocationCallback() {

            override fun onLocationResult(
                result: LocationResult
            ) {
                val location = result.lastLocation ?: return

                latestLocation = location
                onLocationChanged(location)
            }
        }

    @SuppressLint("MissingPermission")
    fun startLocationTracking() {

        val fine =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val coarse =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!fine && !coarse) return

        if (isTracking) {
            latestLocation?.let {
                onCameraToLocation(it)
            }
            return
        }

        isTracking = true

        val request =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
            )
                .setMinUpdateIntervalMillis(1500L)
                .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

                if (location != null) {
                    latestLocation = location
                    onLocationChanged(location)
                    onCameraToLocation(location)
                }
            }

        onTrackingStarted()
    }

    fun stopLocationTracking() {

        if (isTracking) {
            fusedLocationClient.removeLocationUpdates(
                locationCallback
            )

            isTracking = false
        }
    }

    fun getLatestLocation(): Location? {
        return latestLocation
    }
}
