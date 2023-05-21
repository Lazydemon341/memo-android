package com.memo.publish_photo.internal.domain

import android.location.Address
import com.memo.core.location.LocationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class GetLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
) {
    operator fun invoke(): Flow<Address?> {
        return locationProvider.locationUpdatesFlow()
            .onStart {
                locationProvider.lastKnownLocation()?.let { emit(it) }
            }
            .map { locationProvider.getAddress(it) }
    }
}
