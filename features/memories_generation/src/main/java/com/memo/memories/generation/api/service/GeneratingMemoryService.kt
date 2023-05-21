package com.memo.memories.generation.api.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.utils.trySuspend
import com.memo.memories.generation.api.notifications.GeneratedMemoriesNotificationsManager
import com.memo.memories.generation.internal.usecase.GenerateNewMemoriesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GeneratingMemoryService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { coroutineContext, throwable -> })

    @Inject lateinit var generateMemoryUseCase: GenerateNewMemoriesUseCase

    @Inject lateinit var notificationManager: GeneratedMemoriesNotificationsManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startForeground(notificationManager.inProgressNotificationId, notificationManager.getInProgressNotification())
        coroutineScope.launch {
            trySuspend(
                run = {
                    val memoriesCount = generateMemoryUseCase.generate(
                        MemoryRequestParams(
                            numberOfMemories = 5,
                            minPhotosInMemory = 10,
                            maxDaysBetweenMemorableDays = 3,
                        )
                    )
                    if (memoriesCount != 0) {
                        notificationManager.showDoneNotification()
                    }
                },
                catch = {},
                finally = {
                    notificationManager.hideInProgressNotification()
                    stopSelf()
                }
            )
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        coroutineScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
