package com.memo.memories.internal.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.memo.core.design.icons.MemoIconButtonWithLabel
import com.memo.core.design.icons.MemoIconWithLabel
import com.memo.core.design.icons.MemoLikeIcon
import com.memo.core.design.text.MemoRegularText
import com.memo.core.design.text.MemoTitleWithDivider
import com.memo.core.glide.defaultGlideRequestBuilder
import com.memo.features.memories.R
import com.memo.memories.internal.ui.model.PostUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

private const val userPostContentType = "user_post"

internal fun LazyListScope.MainSection(
    photos: List<PostUiState>,
    onPostLikeClick: (Boolean) -> Unit,
    onPostCommentsClick: () -> Unit,
) {
    item(
        contentType = titleWithDividerContentType,
        key = R.string.main_section_title,
    ) {
        MemoTitleWithDivider(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.main_section_title),
        )
    }
    items(
        items = photos,
        contentType = { userPostContentType },
        key = { it.imageUrl },
        itemContent = {
            UserPost(
                postUiState = it,
                onLikeClick = onPostLikeClick,
                onCommentsClick = onPostCommentsClick
            )
        }
    )
}

@Composable
private fun UserPost(
    postUiState: PostUiState,
    onLikeClick: (Boolean) -> Unit,
    onCommentsClick: () -> Unit,
) {
    Column {
        // PostHeader(postUiState)
        Spacer(modifier = Modifier.height(12.dp))
        PostImage(postUiState)
        Spacer(modifier = Modifier.height(4.dp))
        MemoIconWithLabel(
            imageVector = Outlined.LocationOn,
            label = postUiState.location,
            contentDescription = "",
        )
//        PostIcons(
//            postUiState = postUiState,
//            onLikeClick = onLikeClick,
//            onCommentsClick = onCommentsClick,
//        )
    }
}

// @Composable
// private fun PostHeader(postUiState: PostUiState) {
//    Row(
//        modifier = Modifier.padding(top = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Image(
//            modifier = Modifier
//                .size(32.dp)
//                .clip(CircleShape),
//            painter = painterResource(drawable.user_avatar_test),
//            contentDescription = ""
//        )
//        MemoAdditionalText(
//            modifier = Modifier.padding(start = 4.dp),
//            text = postUiState.user.username,
//        )
//        if (postUiState.with.isNotEmpty()) {
//            Spacer(Modifier.weight(1f))
//            MemoCommentText(text = "Фото с")
//            Row(
//                modifier = Modifier.padding(start = 4.dp),
//                horizontalArrangement = Arrangement.spacedBy((-6).dp)
//            ) {
//                postUiState.with.forEach {
//                    Image(
//                        modifier = Modifier
//                            .size(32.dp)
//                            .clip(CircleShape)
//                            .border(width = 2.dp, color = Color.Black, shape = CircleShape),
//                        painter = painterResource(drawable.user_avatar_test),
//                        contentDescription = ""
//                    )
//                }
//            }
//        }
//    }
// }

@Composable
private fun PostImage(postUiState: PostUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        GlideImage(
            modifier = Modifier
                .size(204.dp)
                .clip(RoundedCornerShape(size = 20.dp)),
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillBounds,
            ),
            requestBuilder = { defaultGlideRequestBuilder() },
            imageModel = { postUiState.imageUrl },
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
        )
        MemoRegularText(
            modifier = Modifier.padding(start = 12.dp),
            text = postUiState.description,
        )
    }
}

@Composable
private fun PostIcons(
    postUiState: PostUiState,
    onLikeClick: (Boolean) -> Unit,
    onCommentsClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MemoLikeIcon(
            liked = postUiState.likedByUser,
            label = "${postUiState.likesCount}",
            onClick = onLikeClick,
            contentDescription = "",
        )
        MemoIconButtonWithLabel(
            modifier = Modifier.padding(start = 8.dp),
            imageVector = Outlined.ModeComment,
            label = "${postUiState.commentsCount}",
            onClick = onCommentsClick,
            contentDescription = "",
        )
        Spacer(Modifier.weight(1f))
        MemoIconWithLabel(
            imageVector = Outlined.LocationOn,
            label = postUiState.location,
            contentDescription = "",
        )
    }
}
