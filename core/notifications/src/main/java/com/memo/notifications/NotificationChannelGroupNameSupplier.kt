package com.memo.notifications

import android.content.Context
import androidx.core.util.Supplier
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationChannelGroupNameSupplier @Inject constructor(
    @ApplicationContext private val context: Context
) : Supplier<String> {

    override fun get(): String {
        return context.getString(R.string.notifications_channel_name)
    }
}
