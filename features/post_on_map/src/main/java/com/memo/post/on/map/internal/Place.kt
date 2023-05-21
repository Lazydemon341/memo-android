package com.memo.post.on.map.internal

import androidx.compose.runtime.Immutable

@Immutable
data class PlacesUiState(
    val places: List<Place>
)

data class Place(
    val name: String,
    val description: String,
)
