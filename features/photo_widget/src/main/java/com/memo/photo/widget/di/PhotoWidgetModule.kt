package com.memo.photo.widget.di

import com.memo.photo.widget.PhotoWidgetRepository
import com.memo.photo.widget.PhotoWidgetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PhotoWidgetModule {

    @Binds
    fun bindPhotoWidgetRepository(impl: PhotoWidgetRepositoryImpl): PhotoWidgetRepository
}
