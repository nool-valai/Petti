package com.noolvalai.petti.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.noolvalai.petti.MainActivity
import com.noolvalai.petti.data.repository.StorageRepositoryImpl
import com.noolvalai.petti.domain.repository.StorageScanResult

class StorageScanWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val channelId = "petti_scan_channel"

    override suspend fun doWork(): Result {
        createChannel()
        val repository = StorageRepositoryImpl(context)

        return try {
            repository.getStorageCategories().collect { result ->
                when (result) {
                    is StorageScanResult.Progress -> updateNotification(result.message, isOngoing = true)
                    is StorageScanResult.Success -> updateNotification("Your bento box layout is ready.", isOngoing = false)
                }
            }
            Result.success()
        } catch (e: Exception) {
            updateNotification("Analysis failed.", isOngoing = false)
            Result.failure()
        }
    }

    private fun updateNotification(message: String, isOngoing: Boolean) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle(if (isOngoing) "Analyzing Storage..." else "Scan Complete")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(isOngoing) // Prevents the user from swiping it away while calculating
            .setAutoCancel(!isOngoing)
            .build()

        notificationManager.notify(1, notification) // Overwrites the previous state cleanly
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Storage Scan",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}