package com.memo.core.memories.auto.generation.internal.description.title

import android.content.Context
import com.memo.core.location.LocationProvider
import com.memo.core.memories.auto.generation.R
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class MemoryTitleProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationProvider: LocationProvider
) {

    suspend fun getTitleForPhotos(photos: List<ImageMetaData>): String {
        val addresses = photos.mapNotNull { it.location }.mapNotNull {
            locationProvider.getAddress(it.latitude.toDouble(), it.longitude.toDouble())
        }
        val country = addresses.getOrNull(0)?.countryName
        val city = addresses.getOrNull(0)?.locality
        if (country == null || city == null) {
            return context.getString(R.string.memory_default_title)
        }
        return "$city, $country"
    }
}
