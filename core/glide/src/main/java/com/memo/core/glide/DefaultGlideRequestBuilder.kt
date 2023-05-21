package com.memo.core.glide

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions

@Composable
fun defaultGlideRequestBuilder(): RequestBuilder<*> {
    return Glide
        .with(LocalContext.current)
        .asBitmap()
        .transition(BitmapTransitionOptions.withCrossFade())
}
