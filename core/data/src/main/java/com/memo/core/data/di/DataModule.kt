package com.memo.core.data.di

import com.memo.core.data.repository.FriendshipRepository
import com.memo.core.data.repository.FriendshipRepositoryImpl
import com.memo.core.data.repository.PhotoRepository
import com.memo.core.data.repository.PhotoRepositoryImpl
import com.memo.core.data.repository.UserRepository
import com.memo.core.data.repository.UserRepositoryImpl
import com.memo.core.data.repository.feed.FeedRepository
import com.memo.core.data.repository.feed.FeedRepositoryImpl
import com.memo.core.data.repository.map.MapRepository
import com.memo.core.data.repository.map.MapRepositoryImpl
import com.memo.core.data.repository.memories.GeneratedMemoriesRepository
import com.memo.core.data.repository.memories.GeneratedMemoriesRepositoryImpl
import com.memo.core.data.repository.post.SinglePostRepository
import com.memo.core.data.repository.post.SinglePostRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindTakenImageRepository(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    fun bindFriendshipRepository(impl: FriendshipRepositoryImpl): FriendshipRepository

    @Binds
    fun bindFeedRepository(impl: FeedRepositoryImpl): FeedRepository

    @Binds
    fun bindMapRepository(impl: MapRepositoryImpl): MapRepository

    @Binds
    fun bindSinglePostRepository(impl: SinglePostRepositoryImpl): SinglePostRepository

    @Binds
    fun bindGeneratedMemoriesRepository(impl: GeneratedMemoriesRepositoryImpl): GeneratedMemoriesRepository
}
