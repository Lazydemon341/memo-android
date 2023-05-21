package com.memo.core.memories.auto.generation.api.di

import com.memo.core.memories.auto.generation.api.MemoriesGenerator
import com.memo.core.memories.auto.generation.internal.MemoriesGeneratorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface MemoriesAutoGenerationModule {

    @Binds
    fun bindMemoriesGenerator(impl: MemoriesGeneratorImpl): MemoriesGenerator
}
