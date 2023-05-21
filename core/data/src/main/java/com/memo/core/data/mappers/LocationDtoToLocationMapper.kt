package com.memo.core.data.mappers

import com.memo.core.model.Location
import com.memo.core.network.model.common.LocationDTO

fun LocationDTO.toLocation(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}

fun Location.toLocationDTO(): LocationDTO {
    return LocationDTO(
        latitude = latitude,
        longitude = longitude,
    )
}
