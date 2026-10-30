package com.noolvalai.petti.ui.screens.grid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.noolvalai.petti.data.repository.StorageRepositoryImpl
import com.noolvalai.petti.data.worker.StorageScanWorker
import com.noolvalai.petti.domain.model.StorageCategory
import com.noolvalai.petti.domain.repository.StorageRepository
import com.noolvalai.petti.domain.repository.StorageScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface BentoGridUiState {
    // Add the message parameter to the Loading state
    data class Loading(val message: String) : BentoGridUiState
    data class Success(val categories: List<StorageCategory>) : BentoGridUiState
    data class Error(val message: String) : BentoGridUiState
}

class BentoGridViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StorageRepository = StorageRepositoryImpl(application)
    private val workManager = WorkManager.getInstance(application)

    private val _uiState = MutableStateFlow<BentoGridUiState>(BentoGridUiState.Loading("Initializing..."))
    val uiState: StateFlow<BentoGridUiState> = _uiState.asStateFlow()

    fun analyzeStorage() {
        viewModelScope.launch {
            try {
                repository.getStorageCategories().collect { result ->
                    when (result) {
                        is StorageScanResult.Progress -> {
                            _uiState.value = BentoGridUiState.Loading(result.message)
                        }
                        is StorageScanResult.Success -> {
                            _uiState.value = BentoGridUiState.Success(result.categories)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = BentoGridUiState.Error(e.localizedMessage ?: "Failed to analyze storage")
            }
        }
    }

    fun runScanInBackground() {
        val scanWorkRequest = OneTimeWorkRequestBuilder<StorageScanWorker>().build()
        workManager.enqueue(scanWorkRequest)
    }
}