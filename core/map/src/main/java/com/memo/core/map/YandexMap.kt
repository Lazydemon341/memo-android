package com.memo.core.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView

@Composable
fun YandexMap(
    modifier: Modifier = Modifier,
    content: @Composable (MapView) -> Unit,
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    LaunchedEffect(Unit) {
        MapKitFactory.initialize(context)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { owner, event ->
            when (event) {
                ON_CREATE -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }

                ON_DESTROY -> {
                    MapKitFactory.getInstance().onStop()
                    mapView.onStop()
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    content(mapView)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            mapView
        },
        update = { updatableMapView ->
            updatableMapView
        }
    )
}
