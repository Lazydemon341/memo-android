package com.memo.notifications

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationChannelGroupNameProvider: NotificationChannelGroupNameSupplier,
) : NotificationManager {

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }

    override fun hideNotification(notificationId: Int) {
        notificationManagerCompat.cancel(notificationId)
    }

    override fun showNotification(notificationId: Int, data: NotificationData) {
        val notification = getNotificationForData(data)
        notificationManagerCompat.notify(notificationId, notification)
    }

    override fun getNotificationForData(data: NotificationData): Notification {
        val notificationChannelId = NOTIFICATION_CHANNEL_ID
        val notificationChannelName = getChannelName()
        createOrUpdateNotificationChannelGroup(NOTIFICATION_CHANNEL_GROUP_ID)
        createOrUpdateNotificationChannel(
            notificationChannelId,
            notificationChannelName,
            NOTIFICATION_CHANNEL_GROUP_ID,
        )
        val notificationBuilder = createNotificationBuilder(notificationChannelId, data)
        return notificationBuilder.build()
    }

    private fun createNotificationBuilder(notificationChannelId: String, data: NotificationData): NotificationCompat.Builder {
        val contentIntent = createNotificationContentIntent(data.activityIntent)
        return NotificationCompat.Builder(context, notificationChannelId)
            .setPriority(mapPriorityToSystemValue(data.priority))
            .setSmallIcon(R.drawable.small_notification_icon)
            .setSilent(data.isSilent)
            .setAutoCancel(data.autoCancel)
            .setContentText(data.description)
            .setContentTitle(data.title)
            .setContentIntent(contentIntent)
    }

    private fun mapPriorityToSystemValue(priority: NotificationData.Priority): Int {
        return when (priority) {
            NotificationData.Priority.MIN -> NotificationCompat.PRIORITY_MIN
            NotificationData.Priority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationData.Priority.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
            NotificationData.Priority.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationData.Priority.MAX -> NotificationCompat.PRIORITY_MAX
        }
    }

    override fun canShowNotification(): Boolean {
        if (!notificationManagerCompat.areNotificationsEnabled()) {
            return false
        }
        return areNotificationsPermissionGranted()
    }

    private fun areNotificationsPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun createOrUpdateNotificationChannel(
        id: String,
        name: String,
        groupId: String,
    ) {
        val notificationChannelBuilder =
            NotificationChannelCompat.Builder(
                id,
                NotificationManagerCompat.IMPORTANCE_DEFAULT,
            )
                .setName(name)
                .setGroup(groupId)
        notificationManagerCompat.createNotificationChannel(notificationChannelBuilder.build())
    }

    private fun createOrUpdateNotificationChannelGroup(id: String) {
        val notificationChannelGroupBuilder =
            NotificationChannelGroupCompat.Builder(id)
                .setName(notificationChannelGroupNameProvider.get())
        notificationManagerCompat.createNotificationChannelGroup(
            notificationChannelGroupBuilder.build()
        )
    }

    private fun createNotificationContentIntent(activityIntent: Intent?): PendingIntent? {
        activityIntent ?: return null
        return PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun getChannelName(): String {
        return context.getString(R.string.notifications_channel_name)
    }

    private companion object {

        private const val NOTIFICATION_CHANNEL_GROUP_ID = "memo_notification_channel_group"

        private const val NOTIFICATION_CHANNEL_ID = "memo_notification_channel"
    }
}
