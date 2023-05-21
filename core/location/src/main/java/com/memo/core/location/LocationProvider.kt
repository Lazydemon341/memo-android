package com.memo.core.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

private const val LOCATION_MIN_TIME_MS = 10000L
private const val LOCATION_MIN_DISTANCE = 10f

class LocationProvider @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val locationManager = context.getSystemService<LocationManager>()
    private val geocoder by lazy { Geocoder(context, Locale.getDefault()) }

    fun lastKnownLocation(): Location? {
        locationManager ?: return null
        try {
            return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } else {
                null
            }
        } catch (e: SecurityException) {
            Timber.e(e, "Location access security error")
        } catch (e: Exception) {
            Timber.e(e, "Location access error")
        }
        return null
    }

    @SuppressLint("MissingPermission")
    fun locationUpdatesFlow(): Flow<Location> {
        locationManager ?: return emptyFlow()
        return callbackFlow {
            val listener = LocationListener { trySend(it) }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_MIN_TIME_MS,
                    LOCATION_MIN_DISTANCE,
                    listener,
                    Looper.getMainLooper()
                )
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_MIN_TIME_MS,
                    LOCATION_MIN_DISTANCE,
                    listener,
                    Looper.getMainLooper()
                )
            }
            awaitClose { locationManager.removeUpdates(listener) }
        }
    }

    suspend fun getAddress(location: Location): Address? {
        return getAddress(latitude = location.latitude, longitude = location.longitude)
    }

    suspend fun getAddress(latitude: Double, longitude: Double): Address? {
        return withContext(Dispatchers.IO) {
            geocoder.getFromLocation(
                latitude,
                longitude,
                1
            )?.get(0)
        }
    }
}
