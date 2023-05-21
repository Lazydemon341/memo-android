package com.memo.feed.api

import androidx.paging.PagingSource
import com.memo.core.model.feed.Post
import com.memo.feed.internal.data.PostsPagingSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FeedModule {

    @Binds
    fun providePostsPagingSource(impl: PostsPagingSource): PagingSource<Long, Post>
}
