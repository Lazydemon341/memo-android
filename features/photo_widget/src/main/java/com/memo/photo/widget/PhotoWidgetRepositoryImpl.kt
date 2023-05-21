package com.memo.photo.widget

import com.memo.core.location.LocationProvider
import com.memo.core.network.NetworkWidgetDataSource
import com.memo.core.network.model.common.LocationDTO
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import com.memo.photo.widget.models.LastPhotoInfo
import javax.inject.Inject

class PhotoWidgetRepositoryImpl
@Inject constructor(
    private val networkWidgetDataSource: NetworkWidgetDataSource,
    private val locationProvider: LocationProvider,
) : PhotoWidgetRepository {

    override suspend fun getLastPhoto(): Result<LastPhotoInfo> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                networkWidgetDataSource.getLastPhoto()
            }
            LastPhotoInfo(
                photoUrl = response.postImageUrl,
                caption = response.caption.orEmpty(),
                publisherProfileImageUrl = response.user.photoUrl,
                location = response.location.getLocation(),
            )
        }
    }

    private suspend fun LocationDTO?.getLocation(): String? {
        this ?: return null
        val address = locationProvider.getAddress(latitude.toDouble(), longitude.toDouble())
        val country = address?.countryName
        val city = address?.locality
        if (country == null || city == null) {
            return null
        }
        return "$city, $country"
    }
}
