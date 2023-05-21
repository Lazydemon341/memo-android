package com.memo.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.memo.core.datastore.friendship.Friendship
import com.memo.core.datastore.friendship.FriendshipSerializer
import com.memo.core.datastore.memories.Memories
import com.memo.core.datastore.memories.MemoriesSerializer
import com.memo.core.datastore.user_tokens.UserTokens
import com.memo.core.datastore.user_tokens.UserTokensSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @DataStoreCoroutineScope
    fun provideDataStoreCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    @Provides
    @Singleton
    fun provideUserTokensDataStore(
        @ApplicationContext context: Context,
        @DataStoreCoroutineScope coroutineScope: CoroutineScope,
        userTokensSerializer: UserTokensSerializer,
    ): DataStore<UserTokens> {
        return DataStoreFactory.create(
            serializer = userTokensSerializer,
            scope = coroutineScope,
        ) {
            context.dataStoreFile("user_tokens.pb")
        }
    }

    @Provides
    @Singleton
    fun provideFriendshipDataStore(
        @ApplicationContext context: Context,
        @DataStoreCoroutineScope coroutineScope: CoroutineScope,
        friendshipSerializer: FriendshipSerializer,
    ): DataStore<Friendship> {
        return DataStoreFactory.create(
            serializer = friendshipSerializer,
            scope = coroutineScope,
        ) {
            context.dataStoreFile("friendship.pb")
        }
    }

    @Provides
    @Singleton
    fun provideGeneratedMemoriesDataSource(
        @ApplicationContext context: Context,
        @DataStoreCoroutineScope coroutineScope: CoroutineScope,
        serializer: MemoriesSerializer
    ): DataStore<Memories> {
        return DataStoreFactory.create(
            serializer = serializer,
            scope = coroutineScope,
        ) {
            context.dataStoreFile("memories.pb")
        }
    }
}
