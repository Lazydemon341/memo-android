package com.memo.memories.upload.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.icons.MemoBackIcon
import com.memo.core.design.text.MemoBigTitle
import com.memo.core.design.text.MemoRegularText
import com.memo.features.memories.upload.R
import com.memo.memories.upload.internal.model.MemoryLoadingUiState
import com.memo.memories.upload.internal.model.MemoryUiState
import com.memo.memories.upload.internal.sections.gallerySection
import com.memo.memories.upload.internal.sections.mapSection

@Composable
internal fun MemoryRoute(
    memoryId: Long,
    onBackPressed: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.loadMemoryWithId(memoryId)
    }
    val memoryUiState: MemoryUiState by viewModel.memoryUiState.collectAsStateWithLifecycle()
    val memoryUploadState by viewModel.memoryLoadingState.collectAsStateWithLifecycle()
    if (memoryUploadState is MemoryLoadingUiState.Success) {
        onBackPressed()
    }
    MemoryScreen(
        onBackPressed = onBackPressed,
        memoryUiState = memoryUiState,
        isLoading = memoryUploadState is MemoryLoadingUiState.Loading,
        onUploadClicked = {
            viewModel.uploadMemory(memoryId)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryScreen(
    onBackPressed: () -> Unit,
    onUploadClicked: () -> Unit,
    memoryUiState: MemoryUiState,
    isLoading: Boolean,
) {
    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.Black,
        topBar = { MemoryTopBar(onBackPressed) },
        content = { padding ->
            MemoryContent(
                padding = padding,
                memory = memoryUiState,
                isLoading = isLoading,
                onUploadClicked = onUploadClicked
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryTopBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = {
            MemoBackIcon(
                onClick = onBackPressed,
                contentDescription = "",
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Black,
            navigationIconContentColor = Color.White,
        )
    )
}

@Composable
private fun MemoryContent(
    padding: PaddingValues,
    isLoading: Boolean,
    memory: MemoryUiState,
    onUploadClicked: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            memoryTitle(memory)
            if (memory.mapSection.shouldShowMap) {
                mapSection(
                    mapUiState = memory.mapSection,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapHeight)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(size = 20.dp))
                )
            }
            gallerySection(
                gallerySectionUiState = memory.gallerySection,
            )
            item {
                Spacer(modifier = Modifier.height(uploadButtonHeight + uploadButtonBottomPadding))
            }
        }
        UploadButton(onButtonClicked = onUploadClicked, isLoading = isLoading, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

private fun LazyListScope.memoryTitle(memory: MemoryUiState) {
    item {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MemoBigTitle(text = memory.title)
            Spacer(Modifier.height(16.dp))
            MemoRegularText(
                text = memory.description,
            )
        }
    }
}

@Composable
private fun UploadButton(
    isLoading: Boolean,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        TextButton(
            onClick = onButtonClicked,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.textButtonColors(containerColor = uploadButtonBgColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(uploadButtonHeight)
                .padding(bottom = uploadButtonBottomPadding, start = 12.dp, end = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.upload), color = uploadButtonTextColor, fontSize = 16.sp)
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

private val mapHeight = 200.dp
private val uploadButtonHeight = 64.dp
private val uploadButtonBottomPadding = 16.dp
private val uploadButtonBgColor = Color.LightGray.copy(alpha = 0.9f)
private val uploadButtonTextColor = Color.Black
