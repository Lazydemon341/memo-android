package com.memo.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.memo.app.data.DeeplinkHandler
import com.memo.app.ui.AppRootComposable
import com.memo.core.datastore.memories.GeneratedMemoriesDataSource
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.design.theme.MemoAppTheme
import com.memo.core.location.LocationUtils
import com.memo.core.network.utils.monitor.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var userTokensDataSource: UserTokensDataSource

    @Inject
    lateinit var generatedMemoriesDataSource: GeneratedMemoriesDataSource

    @Inject
    lateinit var deeplinkHandler: DeeplinkHandler

    private var createDestroyScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDestroyScope = MainScope()
        setContent {
            // Здесь только вызывается root composable функция и передаются в нее зависимости
            MemoAppTheme {
                AppRootComposable(networkMonitor, userTokensDataSource, generatedMemoriesDataSource)
            }
        }
        handleIntent(intent)
        requestLocationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        createDestroyScope?.cancel()
    }

    private fun handleIntent(intent: Intent?) {
        handleAppLink(
            appLinkAction = intent?.action,
            appLinkData = intent?.data,
        )
    }

    private fun handleAppLink(appLinkAction: String?, appLinkData: Uri?) {
        if (appLinkAction == Intent.ACTION_VIEW && appLinkData != null) {
            createDestroyScope?.launch {
                deeplinkHandler.handle(uri = appLinkData, context = this@MainActivity)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        locationPermissionRequest.launch(LocationUtils.getLocationPermissions())
    }
}
