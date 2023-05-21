package com.memo.demo.app.photo.meta.inf

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { result ->
        if (result.isNotEmpty()) {
            viewModel.getSelectedPhotosMetaData(context, result)
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) viewModel.permissionAccepted() else viewModel.permissionDenied()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LaunchedEffect(Unit) {
            askForReadPermission(
                context = context, launcher = requestPermissionLauncher
            )
        }

        val permissionDenied by viewModel.permissionDenied.collectAsState()
        val notConnected = stringResource(R.string.permission_denied_message)
        LaunchedEffect(permissionDenied) {
            if (permissionDenied) {
                snackbarHostState.showSnackbar(
                    message = notConnected, duration = SnackbarDuration.Long
                )
            }
        }

        val isReadyToSend by viewModel.isReportReadyToSendFlow.collectAsState()
        val sendMessage = stringResource(R.string.send_data_message)
        LaunchedEffect(isReadyToSend) {
            if (isReadyToSend) {
                sendFile(context, viewModel.filePathForSending, sendMessage)
                viewModel.markDataAsSent()
            }
        }

        val memoriesList by viewModel.memoriesFlow.collectAsState()
        val allPhotosCollected by viewModel.allPhotosInfoCollectedFlow.collectAsState()
        val loading by viewModel.loading.collectAsState()

        MainScreenContent(
            memories = memoriesList,
            allPhotosCollected = allPhotosCollected,
            loading = loading,
            onAllPhotosReportClick = { viewModel.getAllPhotosMetaData(context.applicationContext) },
            onSelectPhotosClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            },
            askForPermissions = {
                askForReadPermission(
                    context = context, launcher = requestPermissionLauncher
                )
            },
            removeMemoryClicked = viewModel::deleteMemoryWithIndex,
            onSendDataClicked = {
                viewModel.saveDataToFile()
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun MainScreenContent(
    memories: List<MemoryInfo>,
    allPhotosCollected: Boolean,
    loading: Boolean,
    onAllPhotosReportClick: () -> Unit,
    onSelectPhotosClick: () -> Unit,
    askForPermissions: () -> Unit,
    removeMemoryClicked: (Int) -> Unit,
    onSendDataClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .padding(top = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { onAllPhotosReportClick() }, Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.collect_all_photos_info))
                    Checkbox(checked = allPhotosCollected, onCheckedChange = {})
                }
            }
            Button(onClick = {
                onSelectPhotosClick()
            }, Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.form_new_memory))
            }
            Button(onClick = {
                onSendDataClicked()
            }, Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.send_data_message))
            }
            Button(onClick = { askForPermissions() }, Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.retry_permission_request))
            }

            LazyColumn(
                contentPadding = PaddingValues(vertical = 4.dp),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            ) {
                if (memories.isEmpty()) {
                    item {
                        Text(text = stringResource(R.string.empty_memories_list_message))
                    }
                } else {
                    items(memories) { memoryInfo ->
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Воспоминание номер ${memoryInfo.index} из ${memoryInfo.photosCount} фото",
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { removeMemoryClicked(memoryInfo.index) }) {
                                Icon(Icons.Filled.Close, contentDescription = "close")
                            }
                        }
                    }
                }
            }
        }

        if (loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(48.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private fun askForReadPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {

    if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_MEDIA_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        launcher.launch(
            Manifest.permission.ACCESS_MEDIA_LOCATION,
        )
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        launcher.launch(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    }

    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        launcher.launch(
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }
}

private fun sendFile(context: Context, filePath: String, message: String) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName!! + ".provider",
        file
    )
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/*"
    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
    context.startActivity(Intent.createChooser(sharingIntent, message))
}
