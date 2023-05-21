package com.memo.memories.internal.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.memo.core.design.StaggeredVerticalGrid
import com.memo.core.design.icons.MemoLikeIcon
import com.memo.core.design.text.MemoTitleWithDivider
import com.memo.core.design.utils.memoShadow
import com.memo.core.glide.defaultGlideRequestBuilder
import com.memo.features.memories.R
import com.memo.memories.internal.ui.model.GalleryImageUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

internal const val titleWithDividerContentType = "title_with_divider"
private const val galleryImagesContentType = "gallery_images"

internal fun LazyListScope.GallerySection(
    photos: List<GalleryImageUiState>,
    onGalleryLikeClick: (Boolean) -> Unit,
) {
    item(
        contentType = titleWithDividerContentType,
        key = R.string.gallery_section_title,
    ) {
        MemoTitleWithDivider(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.gallery_section_title),
        )
    }
    photos.chunked(2).forEach {
        item(
            contentType = galleryImagesContentType,
            key = it.first().imageUrl.plus(it.lastOrNull()?.imageUrl),
        ) {
            GalleryImagesRow(
                modifier = Modifier
                    .padding(top = 12.dp),
                firstImage = it.first(),
                secondImage = it.lastOrNull(),
                onLikeClick = onGalleryLikeClick,
            )
        }
    }
}

@Composable
private fun GalleryImagesRow(
    firstImage: GalleryImageUiState,
    secondImage: GalleryImageUiState?,
    onLikeClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GalleryImage(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f),
            imageUiState = firstImage,
            onLikeClick = onLikeClick,
        )
        secondImage?.let {
            GalleryImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                imageUiState = secondImage,
                onLikeClick = onLikeClick,
            )
        }
    }
}

@Composable
private fun GalleryImages(
    photos: List<GalleryImageUiState>,
    onLikeClick: (Boolean) -> Unit,
) {
    StaggeredVerticalGrid(
        modifier = Modifier.padding(top = 16.dp),
        columns = 2,
        horizontalSpacing = 12.dp,
        verticalSpacing = 12.dp,
        content = {
            photos.forEach { image ->
                GalleryImage(image, onLikeClick)
            }
        }
    )
}

@Composable
private fun GalleryImage(
    imageUiState: GalleryImageUiState,
    onLikeClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        GlideImage(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(size = 20.dp)),
            imageOptions = ImageOptions(
                contentScale = ContentScale.Fit,
            ),
            requestBuilder = { defaultGlideRequestBuilder() },
            imageModel = { imageUiState.imageUrl },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
        MemoLikeIcon(
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .memoShadow(),
            liked = imageUiState.liked,
            onClick = onLikeClick,
            contentDescription = "",
        )
    }
}
