package com.noolvalai.petti.domain.model

enum class StorageSeverity { SAFE, NOTICE, WARNING, CRITICAL }

data class StorageCategory(
    val id: String,
    val name: String,
    val sizeBytes: Long,
    val totalPercentage: Float,
    val severity: StorageSeverity
)