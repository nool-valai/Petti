package com.noolvalai.petti.domain.repository

import com.noolvalai.petti.domain.model.StorageCategory
import kotlinx.coroutines.flow.Flow

sealed interface StorageScanResult {
    data class Progress(val message: String) : StorageScanResult
    data class Success(val categories: List<StorageCategory>) : StorageScanResult
}

interface StorageRepository {
    fun getStorageCategories(): Flow<StorageScanResult>
}