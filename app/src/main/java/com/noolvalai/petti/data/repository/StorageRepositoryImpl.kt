package com.noolvalai.petti.data.repository

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.UserHandle
import android.os.storage.StorageManager
import android.provider.MediaStore
import com.noolvalai.petti.domain.model.StorageCategory
import com.noolvalai.petti.domain.model.StorageSeverity
import com.noolvalai.petti.domain.repository.StorageRepository
import com.noolvalai.petti.domain.repository.StorageScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StorageRepositoryImpl(private val context: Context) : StorageRepository {

    override fun getStorageCategories(): Flow<StorageScanResult> = flow {
        val storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val packageManager = context.packageManager
        val uuid = StorageManager.UUID_DEFAULT

        emit(StorageScanResult.Progress("Calculating Total Device Space..."))
        // 1. Total and Free Space
        val totalBytes = storageStatsManager.getTotalBytes(uuid)
        val freeBytes = storageStatsManager.getFreeBytes(uuid)
        val usedBytes = totalBytes - freeBytes

        emit(StorageScanResult.Progress("Analyzing Installed Apps..."))
        // 2. Calculate Exact App Sizes (Requires PACKAGE_USAGE_STATS permission we added)
        var appsBytes = 0L
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (app in installedApps) {
            try {
                val userHandle = UserHandle.getUserHandleForUid(app.uid)
                val stats = storageStatsManager.queryStatsForPackage(uuid, app.packageName, userHandle)
                appsBytes += stats.appBytes + stats.dataBytes + stats.cacheBytes
            } catch (e: Exception) {
                // Safely ignore system packages that restrict access
            }
        }

        emit(StorageScanResult.Progress("Scanning Media Library..."))
        // 3. Calculate Media Size (Videos, Images, Audio) via native MediaStore indexes
        val mediaBytes = getMediaStoreSize(MediaStore.Video.Media.EXTERNAL_CONTENT_URI) +
                getMediaStoreSize(MediaStore.Images.Media.EXTERNAL_CONTENT_URI) +
                getMediaStoreSize(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)

        emit(StorageScanResult.Progress("Finalizing Bento Layout..."))
        // 4. Calculate System & Remaining Files
        val systemAndOtherBytes = (usedBytes - appsBytes - mediaBytes).coerceAtLeast(0L)

        // Create and sort categories so the largest boxes automatically pack at the top of the grid
        val categories = listOf(
            createCategory("1", "Media", mediaBytes, totalBytes),
            createCategory("2", "Apps", appsBytes, totalBytes),
            createCategory("3", "System & Other", systemAndOtherBytes, totalBytes),
            createCategory("4", "Free Space", freeBytes, totalBytes, isFreeSpace = true)
        ).sortedByDescending { it.sizeBytes }

        emit(StorageScanResult.Success(categories))
    }.flowOn(Dispatchers.IO) // Keeps this heavy math off the UI thread

    /**
     * Instantly sums up file sizes for a given media type using Android's SQLite Media database.
     */
    private fun getMediaStoreSize(uri: Uri): Long {
        var totalSize = 0L
        val projection = arrayOf(MediaStore.MediaColumns.SIZE)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            while (cursor.moveToNext()) {
                totalSize += cursor.getLong(sizeColumn)
            }
        }
        return totalSize
    }

    private fun createCategory(
        id: String,
        name: String,
        sizeBytes: Long,
        totalDeviceBytes: Long,
        isFreeSpace: Boolean = false
    ): StorageCategory {
        val percentage = (sizeBytes.toFloat() / totalDeviceBytes.toFloat()) * 100f

        val severity = when {
            isFreeSpace -> StorageSeverity.SAFE
            percentage >= 40f -> StorageSeverity.CRITICAL
            percentage >= 20f -> StorageSeverity.WARNING
            percentage >= 10f -> StorageSeverity.NOTICE
            else -> StorageSeverity.SAFE
        }

        return StorageCategory(id, name, sizeBytes, percentage, severity)
    }
}