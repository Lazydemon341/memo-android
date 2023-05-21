package com.memo.feed.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.memo.core.model.feed.Post
import com.memo.feed.internal.data.PostsPagingSource
import com.memo.feed.internal.ui.model.PostUiState
import com.memo.feed.internal.ui.model.toPostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val postsPagingSourceProvider: Provider<PostsPagingSource>,
) : ViewModel() {

    val postsPagingFLow: Flow<PagingData<PostUiState>> = Pager<Long, Post>(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { postsPagingSourceProvider.get() },
    ).flow.map { pagingData ->
        pagingData.map { post ->
            post.toPostUiState()
        }
    }.cachedIn(viewModelScope)

    private companion object {
        private const val PAGE_SIZE = 10
    }
}
