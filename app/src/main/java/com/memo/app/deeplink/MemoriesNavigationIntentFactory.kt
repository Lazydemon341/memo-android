package com.memo.app.deeplink

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.memo.app.MainActivity
import com.memo.core.model.Factory
import com.memo.memories.generation.api.memoriesListDeeplink
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MemoriesNavigationIntentFactory @Inject constructor(
    @ApplicationContext private val context: Context
) : Factory<Intent> {

    override fun create(): Intent {
        return Intent(
            Intent.ACTION_VIEW,
            memoriesListDeeplink.toUri(),
            context,
            MainActivity::class.java
        )
    }
}
