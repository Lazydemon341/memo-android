package com.memo.notifications

import android.content.Intent

class NotificationData
private constructor(
    val priority: Priority,
    val isSilent: Boolean,
    val title: String,
    val description: String?,
    val autoCancel: Boolean,
    val activityIntent: Intent?,
) {

    enum class Priority {
        MIN,
        LOW,
        DEFAULT,
        HIGH,
        MAX,
    }

    class Builder(
        private val title: String,
    ) {

        private var priority = Priority.DEFAULT

        private var isSilent = false

        private var autoCancel = false

        private var description: String? = null

        private var activityIntent: Intent? = null

        fun setPriority(priority: Priority) = apply { this.priority = priority }

        fun setSilent(isSilent: Boolean) = apply { this.isSilent = isSilent }

        fun setAutoCancel(autoCancel: Boolean) = apply { this.autoCancel = autoCancel }

        fun setDescription(description: String?) = apply { this.description = description }

        fun setActivityIntent(activityIntent: Intent?) = apply {
            this.activityIntent = activityIntent
        }

        fun build() =
            NotificationData(
                priority,
                isSilent,
                title,
                description,
                autoCancel,
                activityIntent,
            )
    }
}
