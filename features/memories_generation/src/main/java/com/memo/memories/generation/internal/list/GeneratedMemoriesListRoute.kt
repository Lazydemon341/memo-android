package com.memo.memories.generation.internal.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.header.ScreenHeader
import com.memo.core.design.icons.MemoLikeIcon
import com.memo.core.design.utils.memoShadow
import com.memo.core.glide.defaultGlideRequestBuilder
import com.memo.core.model.memories.upload.NavigateToMemoryUploadArguments
import com.memo.features.memories.generation.R
import com.memo.memories.generation.api.permissions.CheckForImagesReadPermission
import com.memo.memories.generation.internal.list.MemoriesListUiState.Empty
import com.memo.memories.generation.internal.list.MemoriesListUiState.NotEmpty
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

private val memoryCardHeight = 96.dp

@Composable
internal fun GeneratedMemoriesListRoute(
    onBackPressed: () -> Unit,
    navigateToMemory: (NavigateToMemoryUploadArguments) -> Unit,
    viewModel: MemoriesListViewModel = hiltViewModel()
) {
    val uiState by viewModel.memoriesListUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingFlow.collectAsStateWithLifecycle()

    CheckForImagesReadPermission {
    }

    Column(Modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(id = R.string.memories_list_screen_header),
            onBackPressed = onBackPressed
        )
        Box(Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(24.dp)
                )
            }
            when (uiState) {
                is Empty -> {
                    EmptyScreenContent(
                        onGenerateMemoryClicked = viewModel::generateMemories
                    )
                }

                is NotEmpty -> {
                    FilledScreenContent(
                        memoriesListUiState = uiState as NotEmpty,
                        onMemoryClicked = { navigateToMemory(NavigateToMemoryUploadArguments(it)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyScreenContent(
    onGenerateMemoryClicked: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f, true)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Text(
                    stringResource(R.string.empty_memories_list_caption),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    stringResource(R.string.generate_new_memory_caption),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.LightGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
        Button(
            onClick = onGenerateMemoryClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                stringResource(R.string.generate_new_memory),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun FilledScreenContent(
    memoriesListUiState: NotEmpty,
    onMemoryClicked: (Long) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(memoriesListUiState.memories) { memoryUiState ->
            MemoryPreviewCard(
                memoryPreviewUiState = memoryUiState,
                onMemoryClicked = onMemoryClicked
            )
        }
    }
}

@Composable
private fun MemoryPreviewCard(
    memoryPreviewUiState: MemoryPreviewUiState,
    onMemoryClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(memoryCardHeight)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onMemoryClicked(memoryPreviewUiState.memoryId) },
    ) {
        GlideImage(
            imageModel = { memoryPreviewUiState.previewPhotoUri },
            imageOptions = ImageOptions(contentScale = ContentScale.FillWidth),
            requestBuilder = { defaultGlideRequestBuilder() },
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(24.dp))
        )
        Column(Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier
                    .memoShadow(),
                text = memoryPreviewUiState.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier
                    .memoShadow(),
                text = memoryPreviewUiState.caption,
                fontSize = 18.sp,
            )
        }
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
