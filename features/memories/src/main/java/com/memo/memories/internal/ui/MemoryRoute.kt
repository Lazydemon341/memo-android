package com.memo.memories.internal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.icons.MemoBackIcon
import com.memo.core.design.text.MemoBigTitle
import com.memo.core.design.text.MemoRegularText
import com.memo.memories.internal.ui.model.GalleryImageUiState
import com.memo.memories.internal.ui.model.MapSectionUiState
import com.memo.memories.internal.ui.model.MemorySuccessUiState
import com.memo.memories.internal.ui.model.MemoryUiState
import com.memo.memories.internal.ui.model.MemoryUiState.Error
import com.memo.memories.internal.ui.model.MemoryUiState.Loading
import com.memo.memories.internal.ui.model.MemoryUiState.Success
import com.memo.memories.internal.ui.model.PostUiState
import com.memo.memories.internal.ui.model.UserUiState
import com.memo.memories.internal.ui.sections.GallerySection
import com.memo.memories.internal.ui.sections.MainSection
import com.memo.memories.internal.ui.sections.MapSection
import kotlin.random.Random

@Composable
internal fun MemoryRoute(
    onBackPressed: () -> Unit,
    viewModel: MemoryViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.loadMemory()
    }
    val memoryUiState = viewModel.memoryUiState.collectAsStateWithLifecycle()
    MemoryScreen(
        onBackPressed = onBackPressed,
        memoryUiStateProvider = memoryUiState::value,
        onPostLikeClick = viewModel::onPostLikeClick,
        onPostCommentsClick = viewModel::onPostCommentsClick,
        onGalleryLikeClick = viewModel::onGalleryLikeClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MemoryScreen(
    onBackPressed: () -> Unit,
    memoryUiStateProvider: () -> MemoryUiState,
    onPostLikeClick: (Boolean) -> Unit,
    onPostCommentsClick: () -> Unit,
    onGalleryLikeClick: (Boolean) -> Unit,
) {
    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.Black,
        topBar = { MemoryTopBar(onBackPressed) },
        content = { padding ->
            when (val it = memoryUiStateProvider()) {
                Error -> {
                    // TODO
                }

                Loading -> {
                    // TODO
                }

                is Success -> {
                    MemoryContent(
                        padding = padding,
                        memory = it.memory,
                        onPostLikeClick = onPostLikeClick,
                        onPostCommentsClick = onPostCommentsClick,
                        onGalleryLikeClick = onGalleryLikeClick,
                    )
                }
            }
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
    memory: MemorySuccessUiState,
    onPostLikeClick: (Boolean) -> Unit,
    onPostCommentsClick: () -> Unit,
    onGalleryLikeClick: (Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            MemoBigTitle(text = memory.title)
        }
        if (memory.mapUiState.shouldShowMap) {
            MapSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(size = 20.dp)),
                mapUiState = memory.mapUiState,
            )
        }
        MemoryDescription(memory)
        // UsersSection(memory.users)
        MainSection(
            photos = memory.mainPhotos,
            onPostLikeClick = onPostLikeClick,
            onPostCommentsClick = onPostCommentsClick
        )
        GallerySection(
            photos = memory.galleryPhotos,
            onGalleryLikeClick = onGalleryLikeClick,
        )
    }
}

private const val memoryDescriptionContentType = "memory_description"

private fun LazyListScope.MemoryDescription(memory: MemorySuccessUiState) {
    if (memory.shouldShowDescription) {
        item(
            contentType = memoryDescriptionContentType,
        ) {
            MemoRegularText(
                modifier = Modifier.padding(top = 16.dp),
                text = memory.description,
            )
        }
    }
}

@Preview
@Composable
private fun MemoryScreenPreview() {
    MemoryScreen(
        onBackPressed = {},
        memoryUiStateProvider = { Success(previewMemory) },
        onPostLikeClick = {},
        onPostCommentsClick = {},
        onGalleryLikeClick = {},
    )
}

private val previewMemory = MemorySuccessUiState(
    title = "Helsinki",
    description = "Зимний уикенд 2022 в Хельсинки глазами 103 студентов. " +
        "Подписывайся, чтобы следить за нашими приключениями в Финляндии!",
//    users = UsersUiState(
//        users = MutableList(4) { UserUiState(image = Unit, username = "viktoria_design") },
//        description = "anna.slav, gregory_ec, lovia.mart_ \n" +
//            "и ещё 36 друзей — участники",
//    ),
    mainPhotos = listOf(
        PostUiState(
            imageUrl = "",
            user = UserUiState(image = Unit, username = "ksu_01_malts"),
            with = emptyList(),
            description = "Ребята, я влюбилась!!!",
            likedByUser = Random.nextBoolean(),
            likesCount = 102,
            commentsCount = 4,
            location = "Vilhonkatu 13, Helsinki",
        ),
        PostUiState(
            imageUrl = "",
            user = UserUiState(image = Unit, username = "viktoria_design"),
            with = listOf(Unit, Unit, Unit),
            description = "Мама была права, нужно было шапку надеть...",
            likedByUser = Random.nextBoolean(),
            likesCount = 45,
            commentsCount = 2,
            location = "Liisankatu 18, Helsinki",
        )
    ),
    galleryPhotos = buildList {
        repeat(20) {
            add(GalleryImageUiState(imageUrl = "", liked = Random.nextBoolean()))
        }
    },
    mapUiState = MapSectionUiState.EMPTY,
)
