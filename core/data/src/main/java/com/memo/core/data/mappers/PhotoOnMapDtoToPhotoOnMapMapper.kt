package com.memo.core.data.mappers

import com.memo.core.model.map.PhotoOnMap
import com.memo.core.network.model.map.PhotoOnMapDTO

internal fun PhotoOnMapDTO.toPhotoOnMap(): PhotoOnMap {
    return PhotoOnMap(
        location = locationDTO.toLocation(),
        photoUrl = photoUrl,
        postId = postId,
        userId = userId,
    )
}
