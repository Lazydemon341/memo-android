package com.memo.memories.upload.internal.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.memo.core.design.StaggeredVerticalGrid
import com.memo.core.design.text.MemoTitleWithDivider
import com.memo.core.glide.defaultGlideRequestBuilder
import com.memo.features.memories.upload.R.string
import com.memo.memories.upload.internal.model.GallerySectionUiState
import com.memo.memories.upload.internal.model.PhotoUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

internal fun LazyListScope.gallerySection(
    gallerySectionUiState: GallerySectionUiState,
) {
    item {
        MemoTitleWithDivider(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(string.gallery_section_title),
        )
    }
    item {
        GalleryImages(
            imagesUiStates = gallerySectionUiState.images,
        )
    }
}

@Composable
private fun GalleryImages(
    imagesUiStates: List<PhotoUiState>,
) {
    StaggeredVerticalGrid(
        modifier = Modifier.padding(top = 16.dp),
        columns = 2,
        horizontalSpacing = 12.dp,
        verticalSpacing = 12.dp,
        content = {
            imagesUiStates.forEach { image ->
                GalleryImage(image)
            }
        }
    )
}

@Composable
private fun GalleryImage(
    photoUiState: PhotoUiState,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GlideImage(
            imageModel = { photoUiState.photoUri },
            requestBuilder = { defaultGlideRequestBuilder() },
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillBounds,
                requestSize = IntSize(500, 500)
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(12.dp)),
            loading = {
                Box(modifier = Modifier.matchParentSize()) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
        )
        if (photoUiState.shouldShowDescription) {
            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(textBgColor)
                    .padding(4.dp),
                text = photoUiState.description,
                color = Color.White
            )
        }
    }
}

private val textBgColor = Color.DarkGray.copy(alpha = 0.6f)
