package com.memo.chats.list.api.di

import com.memo.chats.list.api.data.ChatsListRepositoryImpl
import com.memo.chats.list.api.domain.ChatsListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ChatsListModule {
    @Binds
    fun bindChatsListRepository(impl: ChatsListRepositoryImpl): ChatsListRepository
}
