package com.memo.memories.generation.api.notifications

import android.app.Notification
import android.content.Context
import android.content.Intent
import com.memo.core.model.Factory
import com.memo.features.memories.generation.R.string
import com.memo.notifications.MemoNotificationId
import com.memo.notifications.NotificationData
import com.memo.notifications.NotificationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

class GeneratedMemoriesNotificationsManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
    @Named("MemoriesNavigationIntentFactory") private val navigationIntentFactory: Factory<Intent>,
) {

    val inProgressNotificationId = MemoNotificationId.MEMORIES_GENERATION_IN_PROGRESS

    private val inProgressNotificationData = NotificationData.Builder(context.getString(string.notification_title_in_progress))
        .setSilent(true)
        .setAutoCancel(true)
        .setDescription(context.getString(string.notification_caption_in_progress))
        .build()

    fun getInProgressNotification(): Notification {
        return notificationManager.getNotificationForData(inProgressNotificationData)
    }

    fun hideInProgressNotification() {
        notificationManager.hideNotification(
            MemoNotificationId.MEMORIES_GENERATION_IN_PROGRESS,
        )
    }

    fun showDoneNotification() {
        notificationManager.showNotification(
            MemoNotificationId.MEMORIES_GENERATION_DONE,
            NotificationData.Builder(context.getString(string.notification_title_generating_done))
                .setSilent(false).setAutoCancel(false)
                .setActivityIntent(navigationIntentFactory.create())
                .setDescription(context.getString(string.notification_caption_generating_done))
                .build()
        )
    }
}
