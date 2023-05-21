package com.memo.memories.upload.internal.sections

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.memo.core.design.icons.MemoBackIcon
import com.memo.core.map.YandexMap
import com.memo.memories.upload.internal.model.MapSectionUiState
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import kotlin.math.roundToInt

private val imageCornerRadius = 12.dp
private val imageSize = 48.dp

internal fun LazyListScope.mapSection(
    mapUiState: MapSectionUiState,
    modifier: Modifier = Modifier
) {
    item {
        MiniMap(mapUiState, modifier)
    }
}

@Composable
private fun MiniMap(
    mapUiState: MapSectionUiState,
    modifier: Modifier = Modifier
) {
    var showFullScreen by remember { mutableStateOf(false) }
    FullScreenMap(
        mapUiState = mapUiState,
        shouldShow = showFullScreen,
        onDismiss = { showFullScreen = false },
    )
    Map(
        modifier = modifier,
        mapUiState = mapUiState,
        disableGestures = true,
        inputListener = object : InputListener {
            override fun onMapTap(p0: Map, p1: Point) {
                showFullScreen = true
            }

            override fun onMapLongTap(p0: Map, p1: Point) = Unit
        },
    )
}

@Composable
private fun FullScreenMap(
    mapUiState: MapSectionUiState,
    shouldShow: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (shouldShow) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false,
            )
        ) {
            Box {
                Map(
                    modifier = modifier,
                    mapUiState = mapUiState,
                    inputListener = object : InputListener {
                        override fun onMapTap(p0: Map, p1: Point) = Unit

                        override fun onMapLongTap(p0: Map, p1: Point) = Unit
                    },
                    disableGestures = false,
                )
                MemoBackIcon(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    onClick = onDismiss,
                    tint = Color.Black,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun Map(
    mapUiState: MapSectionUiState,
    disableGestures: Boolean,
    inputListener: InputListener,
    modifier: Modifier = Modifier,
) {
    YandexMap(
        modifier.fillMaxSize(),
    ) { mapView ->
        val context = LocalContext.current
        mapView.map.apply {
            if (disableGestures) {
                isZoomGesturesEnabled = false
                isScrollGesturesEnabled = false
                isRotateGesturesEnabled = false
                isTiltGesturesEnabled = false
            }
            addInputListener(inputListener)
        }

        val mapObjects = remember { mapView.map.mapObjects.addCollection() }

        LaunchedEffect(mapUiState.boundingBox) {
            var cameraPosition = mapView.map.cameraPosition(mapUiState.boundingBox)
            cameraPosition = CameraPosition(
                cameraPosition.target,
                cameraPosition.zoom - 0.8f,
                cameraPosition.azimuth,
                cameraPosition.tilt
            )
            mapView.map.move(cameraPosition)
        }

        val imageCornerRadiusInPx = with(LocalDensity.current) {
            imageCornerRadius.toPx()
        }.roundToInt()
        val imageSize = with(LocalDensity.current) {
            imageSize.toPx()
        }.roundToInt()

        LaunchedEffect(mapUiState.photos) {
            val photos = mapUiState.photos
            mapObjects.clear()
            photos.forEach { photo ->
                if (photo.photoLocation == null) {
                    return@forEach
                }
                val photoPoint = Point(
                    photo.photoLocation.latitude.toDouble(),
                    photo.photoLocation.longitude.toDouble()
                )
                Glide.with(context)
                    .load(photo.photoUri)
                    .override(imageSize)
                    .transform(RoundedCorners(imageCornerRadiusInPx))
                    .into(object : CustomTarget<Drawable>() {

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            val placemark = mapObjects.addPlacemark(photoPoint)
                            placemark.setIcon(ImageProvider.fromBitmap(resource.toBitmap()))
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            addPlaceholderDrawable(placeholder)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            addPlaceholderDrawable(errorDrawable)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            addPlaceholderDrawable(placeholder)
                        }

                        private fun addPlaceholderDrawable(placeholder: Drawable?) {
                            placeholder?.let {
                                mapObjects.addPlacemark(
                                    photoPoint,
                                    ImageProvider.fromBitmap(it.toBitmap(imageSize, imageSize))
                                )
                            }
                        }
                    })
            }
        }
    }
}
