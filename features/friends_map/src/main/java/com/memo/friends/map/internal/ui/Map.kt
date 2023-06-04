package com.memo.friends.map.internal.ui

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.memo.core.map.YandexMap
import com.memo.friends.map.internal.MapUiState
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import kotlin.math.roundToInt

private val imageCornerRadius = 16.dp
private val imageSize = 48.dp

@Composable
internal fun Map(
    mapUiState: MapUiState,
    onPhotoClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    YandexMap(modifier.fillMaxSize()) { mapView ->
        val context = LocalContext.current

        val mapObjects = remember { mapView.map.mapObjects.addCollection() }

        LaunchedEffect(mapUiState.cameraLocation) {
            mapView.map.move(
                CameraPosition(mapUiState.cameraLocation.cameraPoint, mapUiState.cameraLocation.zoom, 0.0f, 0.0f)
            )
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
                val tapListener = MapObjectTapListener { _, _ ->
                    onPhotoClicked(photo.postId)
                    return@MapObjectTapListener true
                }

                Glide.with(context)
                    .load(photo.photoUrl)
                    // .placeholder(R.drawable.profile_avatar_placeholder)
                    // .error(R.drawable.profile_avatar_placeholder)
                    .override(imageSize)
                    .transform(RoundedCorners(imageCornerRadiusInPx))
                    .into(object : CustomTarget<Drawable>() {

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            val placemark = mapObjects.addPlacemark(photo.photoPoint)
                            placemark.setIcon(ImageProvider.fromBitmap(resource.toBitmap()))
                            placemark.addTapListener(tapListener)
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
                                    photo.photoPoint,
                                    ImageProvider.fromBitmap(it.toBitmap(imageSize, imageSize))
                                )
                            }
                        }
                    })
            }
        }
    }
}
