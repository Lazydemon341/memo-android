package com.memo.post.on.map.internal

import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PageSize.Fill
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.memo.core.design.icons.MemoIcons
import com.memo.post.on.map.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PostOnMap(
    postId: Long,
    onBackPressed: () -> Unit,
    viewModel: PostOnMapViewModel = hiltViewModel()
) {
    val post = viewModel.postUiStateFlow.collectAsStateWithLifecycle()
    val places = viewModel.nearByPlaces.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getPostById(postId)
    }

    Box() {
        VerticalPager(
            pageCount = 2,
            pageSize = Fill,
            state = rememberPagerState(),
            beyondBoundsPageCount = 1,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            if (index == 0) {
                Box(Modifier.fillMaxSize()) {
                    if (post.value == null) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        Post(post = post.value!!)
                    }
                }
            } else {
                PlacesNearByPage(places = places.value)
            }
        }
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                MemoIcons.ArrowBack, contentDescription = "", tint = Color.White
            )
        }
    }
}

@Composable
private fun Post(post: PostUiState, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Box {
                PostImage(postImageUrl = post.postImageUrl)
                if (post.shouldShowCaption) {
                    Caption(caption = post.caption!!, modifier = Modifier.padding(16.dp).align(Alignment.BottomStart))
                }
            }
            PublicationInfo(
                profilePhotoUrl = post.profileImageUrl,
                profileName = post.profileName,
                postTime = post.publicationTimeInMillis,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(stringResource(R.string.post_image_places_near_tip))
            Icon(Icons.Default.ArrowDropDown, "")
        }
    }
}

@Composable
private fun PlacesNearByPage(places: PlacesUiState?, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .padding(top = 48.dp)
    ) {
        if (places != null) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
            ) {
                places.places.forEach {
                    PlaceNearBy(name = it.name, description = it.description)
                }
            }
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.Center)
            ) {
                Text(stringResource(R.string.post_image_places_loading_error))
            }
        }
    }
}

@Composable
private fun PlaceNearBy(name: String, description: String, modifier: Modifier = Modifier) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = name,
            maxLines = 1,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = modifier
        )
        Text(text = description, maxLines = 1, fontSize = 16.sp, modifier = modifier)
    }
}

@Composable
private fun PublicationInfo(
    profilePhotoUrl: String?,
    profileName: String,
    postTime: Long,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        GlideImage(
            imageModel = { profilePhotoUrl },
            requestBuilder = { defaultGlideRequestBuilder() },
            loading = {
                Icon(
                    MemoIcons.Profile, null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .aspectRatio(ratio = 1f)
                )
            },
            failure = {
                Icon(
                    MemoIcons.Profile, null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(32.dp)
                        .aspectRatio(ratio = 1f)
                )
            },
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .aspectRatio(ratio = 1f)
        )
        Text(
            text = profileName,
            color = Color.LightGray,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Text(
            text = DateUtils.getRelativeTimeSpanString(
                postTime,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString(),
            color = Color.LightGray,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .weight(1f, true)
        )
    }
}

@Composable
private fun PostImage(postImageUrl: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        GlideImage(
            imageModel = { postImageUrl },
            requestBuilder = { defaultGlideRequestBuilder() },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(ratio = 1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(48.dp)),
            loading = {
                Box(modifier = Modifier.matchParentSize()) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            },
            failure = {
                Text(text = stringResource(R.string.post_image_loading_error))
            }
        )
    }
}

@Composable
private fun Caption(caption: String, modifier: Modifier) {
    Text(
        text = caption, color = Color.Black, fontSize = 16.sp,
        modifier = modifier.padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(captionBgColor)
            .padding(8.dp)
    )
}

private val captionBgColor = Color.LightGray.copy(alpha = 0.8f)

@Composable
private fun defaultGlideRequestBuilder(): RequestBuilder<*> {
    return Glide
        .with(LocalContext.current)
        .asBitmap()
        .transition(BitmapTransitionOptions.withCrossFade())
}
