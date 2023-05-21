package com.memo.app.di

import android.content.Intent
import com.memo.app.deeplink.MemoriesNavigationIntentFactory
import com.memo.core.model.Factory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Named("MemoriesNavigationIntentFactory")
    fun bindsMemoriesNavigationIntentFactory(impl: MemoriesNavigationIntentFactory): Factory<Intent>
}
