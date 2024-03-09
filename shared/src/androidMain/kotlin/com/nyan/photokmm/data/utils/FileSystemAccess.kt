package com.nyan.photokmm.data.utils

import android.app.Application
import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class LocalStorageManagerAndroid(private val context: Context) : FileSystemAccess {
    override suspend fun saveFile(fileName: String, data: ByteArray): Boolean {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
        val file = File(path, fileName)
        return try {
            withContext(Dispatchers.IO) {
                FileOutputStream(file).use { it.write(data) }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getFile(fileName: String): ByteArray? {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
        val file = File(path, fileName)
        return if (file.exists()) file.readBytes() else null
    }
}

actual fun provideFileSystemAccess(): FileSystemAccess = LocalStorageManagerAndroid(Application().baseContext)