package com.memo.notifications

import androidx.annotation.IntDef
import kotlin.annotation.AnnotationRetention.SOURCE

@IntDef(
    MemoNotificationId.MEMORIES_GENERATION_IN_PROGRESS,
    MemoNotificationId.MEMORIES_GENERATION_DONE,
)
@Retention(SOURCE)
annotation class MemoNotificationId {
    companion object {
        const val MEMORIES_GENERATION_IN_PROGRESS = 101
        const val MEMORIES_GENERATION_DONE = 102
    }
}
