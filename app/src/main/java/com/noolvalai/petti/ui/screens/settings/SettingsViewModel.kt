package com.noolvalai.petti.ui.screens.settings

import android.app.Application
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noolvalai.petti.util.formatBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _scannedFolderSize = MutableStateFlow<String?>(null)
    val scannedFolderSize: StateFlow<String?> = _scannedFolderSize.asStateFlow()

    fun analyzeSpecificFolder(uri: Uri) {
        viewModelScope.launch {
            _scannedFolderSize.value = "Calculating..."

            val totalSizeBytes = withContext(Dispatchers.IO) {
                val documentTree = DocumentFile.fromTreeUri(getApplication(), uri)
                calculateDirectorySize(documentTree)
            }

            _scannedFolderSize.value = "Folder Size: ${formatBytes(totalSizeBytes)}"
        }
    }

    /**
     * Recursively traverses the SAF document tree to sum up all file sizes.
     */
    private fun calculateDirectorySize(dir: DocumentFile?): Long {
        if (dir == null || !dir.exists()) return 0L
        var size = 0L

        dir.listFiles().forEach { file ->
            if (file.isDirectory) {
                size += calculateDirectorySize(file)
            } else {
                size += file.length()
            }
        }
        return size
    }
}