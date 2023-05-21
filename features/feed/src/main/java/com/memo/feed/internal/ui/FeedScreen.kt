package com.memo.feed.internal.ui

import android.os.Build
import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.memo.core.design.icons.MemoIcons
import com.memo.core.design.pull.refresh.PullRefreshIndicator
import com.memo.core.design.pull.refresh.pullRefresh
import com.memo.core.design.pull.refresh.rememberPullRefreshState
import com.memo.feed.R
import com.memo.feed.internal.ui.model.PostUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

private const val numberOfPageToPreload = 3

@Composable
internal fun FeedScreen(
    onNavigateToUserProfile: (Long) -> Unit,
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
    FeedScreenContent(
        onNavigateToUserProfile = onNavigateToUserProfile,
        feedUiState = viewModel.postsPagingFLow.collectAsLazyPagingItems(),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeedScreenContent(
    onNavigateToUserProfile: (Long) -> Unit,
    feedUiState: LazyPagingItems<PostUiState>,
) {
    val isRefreshing by remember {
        derivedStateOf {
            feedUiState.loadState.refresh is LoadState.Loading ||
                feedUiState.loadState.append is LoadState.Loading
        }
    }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, feedUiState::refresh)
    Box(
        Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        VerticalPager(
            pageCount = feedUiState.itemCount,
            pageSize = PageSize.Fill,
            state = rememberPagerState(),
            beyondBoundsPageCount = numberOfPageToPreload,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            Post(post = feedUiState[index], onNavigateToUserProfile = onNavigateToUserProfile)
        }
        PullRefreshIndicator(
            isRefreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun Post(
    post: PostUiState?,
    onNavigateToUserProfile: (Long) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        BackgroundBlurredImage(backgroundImageUrl = post?.postImageUrl.orEmpty())
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Box {
                    PostImage(postImageUrl = post?.postImageUrl.orEmpty())
                    if (post?.shouldShowCaption == true) {
                        Caption(caption = post.caption!!, modifier = Modifier.padding(16.dp).align(Alignment.BottomStart))
                    }
                }
                PublicationInfo(
                    profilePhotoUrl = post?.profileImageUrl.orEmpty(),
                    profileName = post?.profileName.orEmpty(),
                    postTime = post?.publicationTimeInMillis ?: 0,
                    profileId = post?.profileId ?: 0,
                    onNavigateToUserProfile = onNavigateToUserProfile,
                )
            }
        }
    }
}

@Composable
private fun PublicationInfo(
    profilePhotoUrl: String,
    profileName: String,
    postTime: Long,
    profileId: Long,
    onNavigateToUserProfile: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp).clickable {
                onNavigateToUserProfile(profileId)
            }
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
                Text(text = stringResource(R.string.image_loading_error))
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

@Composable
private fun BackgroundBlurredImage(backgroundImageUrl: String, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .blur(150.dp)
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlideImage(
                imageModel = { backgroundImageUrl },
                imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
                requestBuilder = { defaultGlideRequestBuilder() },
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
            )
        }
    }
}

private val captionBgColor = Color.LightGray.copy(alpha = 0.8f)

@Composable
private fun defaultGlideRequestBuilder(): RequestBuilder<*> {
    return Glide
        .with(LocalContext.current)
        .asBitmap()
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
        .transition(withCrossFade())
}

@Preview
@Composable
private fun PreviewFeedScreen() {
    FeedScreen({})
}
