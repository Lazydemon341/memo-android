package com.memo.demo.app.photo.meta.inf

import android.content.Context
import android.net.Uri
import androidx.core.util.Supplier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class PhotosReportUiState(
    val index: Int,
    val photosCount: Int,
    val metaInfoForEachPhoto: List<String>
)

data class MemoryInfo(val index: Int, val photosCount: Int)

class MainScreenViewModel(
    private val metaDataReporter: PhotosMetaDataReporter,
    private val reportWriter: ReportWriter,
    private val filePathSupplier: Supplier<String>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private var memoryIndex = 1

    private val mutableMemoriesList: MutableStateFlow<List<PhotosReportUiState>> =
        MutableStateFlow(emptyList())
    val memoriesFlow: StateFlow<List<MemoryInfo>> = mutableMemoriesList.map { memoriesList ->
        memoriesList.map { photoReport ->
            MemoryInfo(
                index = photoReport.index,
                photosCount = photoReport.photosCount
            )
        }
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val mutableAllPhotosMetaData: MutableStateFlow<PhotosReportUiState?> =
        MutableStateFlow(null)
    val allPhotosInfoCollectedFlow: StateFlow<Boolean> = mutableAllPhotosMetaData.map { metaData ->
        metaData != null
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val mutableLoading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = mutableLoading

    private val mutablePermissionDenied = MutableStateFlow(false)
    val permissionDenied: StateFlow<Boolean> = mutablePermissionDenied

    private val mutableIsReportReadyToSendFlow = MutableStateFlow(false)
    val isReportReadyToSendFlow: StateFlow<Boolean> = mutableIsReportReadyToSendFlow

    val filePathForSending: String get() = filePathSupplier.get()

    private val memoriesReport: String
        get() {
            return reportWriter.getReport(
                mutableMemoriesList.value.map {
                    it.metaInfoForEachPhoto
                }.map(reportWriter::getMemoryReport),
                mutableAllPhotosMetaData.value?.metaInfoForEachPhoto?.let(reportWriter::getAllPhotosReport)
                    ?: ""
            )
        }

    fun getAllPhotosMetaData(context: Context) {

        viewModelScope.launch(ioDispatcher) {
            mutableLoading.value = true

            val result = metaDataReporter.getAllImagesInfo(context)
            mutableAllPhotosMetaData.update {

                PhotosReportUiState(
                    index = 0,
                    photosCount = result.size, metaInfoForEachPhoto = result
                )
            }

            mutableLoading.value = false
        }
    }

    fun getSelectedPhotosMetaData(context: Context, imagesUris: List<Uri>) {

        viewModelScope.launch(ioDispatcher) {

            val result = metaDataReporter.getSelectedImagesMetaInfo(context, imagesUris)
            mutableMemoriesList.update { sourceList ->

                val newList = sourceList.toMutableList()
                newList.add(
                    PhotosReportUiState(
                        index = memoryIndex,
                        photosCount = result.size, metaInfoForEachPhoto = result
                    )
                )
                memoryIndex++
                newList
            }
        }
    }

    fun deleteMemoryWithIndex(index: Int) {
        mutableMemoriesList.update { sourceList ->
            val newList = sourceList.toMutableList()
            newList.removeIf { it.index == index }
            newList
        }
    }

    fun deleteAllPhotosReport() {
        mutableAllPhotosMetaData.value = null
    }

    fun saveDataToFile() {
        viewModelScope.launch(ioDispatcher) {
            mutableLoading.value = true

            File(filePathForSending).bufferedWriter().use { wr ->
                wr.write(memoriesReport)
            }

            mutableLoading.value = false
            mutableIsReportReadyToSendFlow.value = true
        }
    }

    fun permissionAccepted() {
        mutablePermissionDenied.value = false
    }

    fun permissionDenied() {
        mutablePermissionDenied.value = true
    }

    fun markDataAsSent() {
        mutableIsReportReadyToSendFlow.value = false
    }
}
