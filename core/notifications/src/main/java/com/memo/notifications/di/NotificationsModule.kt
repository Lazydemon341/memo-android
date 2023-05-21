package com.memo.notifications.di

import com.memo.notifications.NotificationManager
import com.memo.notifications.NotificationManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotificationsModule {

    @Binds
    fun bindsNotificationsManager(impl: NotificationManagerImpl): NotificationManager
}
