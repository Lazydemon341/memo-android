package com.memo.user.profile.internal.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memo.core.design.icons.MemoLikeIcon
import com.memo.core.design.utils.memoShadow
import com.memo.features.user_profile.R.drawable
import com.memo.user.profile.internal.ui.model.UserProfileMemoryUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
internal fun ProfileMemoriesList(
    listState: LazyListState,
    onMemoryClick: (Long) -> Unit,
    memoriesProvider: () -> List<UserProfileMemoryUiState>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = memoriesProvider()) { memory ->
            ProfileMemory(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                onMemoryClick = onMemoryClick,
                memoryUiState = memory,
            )
        }
    }
}

@Composable
private fun ProfileMemory(
    memoryUiState: UserProfileMemoryUiState,
    onMemoryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onMemoryClick.invoke(memoryUiState.id)
            },
    ) {
        GlideImage(
            modifier = Modifier
                .matchParentSize(),
            imageModel = { memoryUiState.photoPreviewUrl },
            imageOptions = ImageOptions(
                contentScale = ContentScale.FillWidth,
            ),
            failure = {
                Image(
                    painter = painterResource(id = drawable.profile_post_preview),
                    contentDescription = ""
                )
            },
        )
        Text(
            modifier = Modifier
                .padding(8.dp)
                .memoShadow(),
            text = memoryUiState.name,
            fontSize = 18.sp,
        )
        MemoLikeIcon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .memoShadow(),
            liked = false,
            onClick = {
                // TODO
            },
            contentDescription = "",
        )
    }
}
