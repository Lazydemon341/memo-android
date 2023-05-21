package com.memo.memories.generation.internal.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.utils.trySuspend
import com.memo.memories.generation.internal.usecase.GenerateNewMemoriesUseCase
import com.memo.memories.generation.internal.usecase.GetGeneratedMemoriesStreamFromStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MemoriesListViewModel @Inject constructor(
    streamUseCase: GetGeneratedMemoriesStreamFromStorageUseCase,
    private val generateMemoriesUseCase: GenerateNewMemoriesUseCase
) : ViewModel() {

    private val mutableIsLoading = MutableStateFlow(false)
    val isLoadingFlow = mutableIsLoading.asStateFlow()

    val memoriesListUiState =
        streamUseCase.memoriesFlow.map { generatedMemoriesList ->
            if (generatedMemoriesList.isEmpty()) {
                MemoriesListUiState.Empty
            } else {
                MemoriesListUiState.NotEmpty(generatedMemoriesList.map { it.toMemoryPreviewUiState() })
            }
        }.flowOn(Dispatchers.IO).distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, MemoriesListUiState.Empty)

    fun generateMemories() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableIsLoading.value = true
            trySuspend(
                run = {
                    generateMemoriesUseCase.generate(
                        MemoryRequestParams(
                            numberOfMemories = 5,
                            minPhotosInMemory = 10,
                            maxDaysBetweenMemorableDays = 3,
                        )
                    )
                },
                catch = {},
                finally = { mutableIsLoading.value = false }
            )
        }
    }
}
