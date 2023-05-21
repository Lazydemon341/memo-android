package com.memo.feed.internal.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.memo.core.data.repository.feed.FeedRepository
import com.memo.core.model.feed.Post
import java.util.concurrent.CancellationException
import javax.inject.Inject

class PostsPagingSource @Inject constructor(
    private val feedRepository: FeedRepository
) : PagingSource<Long, Post>() {

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        return try {
            val fromId = params.key
            val result = feedRepository.getPosts(fromId = fromId, size = params.loadSize)
            val data = result.getOrThrow()
            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = data.last().postId
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
