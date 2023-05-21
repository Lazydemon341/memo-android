package com.memo.publish_photo.internal.domain

import com.memo.core.location.LocationProvider
import com.memo.core.model.Location
import com.memo.publish_photo.internal.data.PublishPhotoRepository
import javax.inject.Inject

class PublishPhotoUseCase @Inject constructor(
    private val repository: PublishPhotoRepository,
    private val locationProvider: LocationProvider,
) {
    suspend operator fun invoke(): Result<Unit> {
        val location = locationProvider.lastKnownLocation()?.let {
            Location(
                latitude = it.latitude.toFloat(),
                longitude = it.longitude.toFloat(),
            )
        }
        return repository.publishPhoto(location)
    }
}
