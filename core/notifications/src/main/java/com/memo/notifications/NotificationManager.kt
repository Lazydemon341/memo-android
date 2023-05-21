package com.memo.notifications

import android.app.Notification

interface NotificationManager {

    fun hideNotification(@MemoNotificationId notificationId: Int)

    fun showNotification(@MemoNotificationId notificationId: Int, data: NotificationData)

    fun canShowNotification(): Boolean

    fun getNotificationForData(data: NotificationData): Notification
}
