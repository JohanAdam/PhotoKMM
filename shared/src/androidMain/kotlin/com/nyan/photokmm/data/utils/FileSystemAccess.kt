package com.nyan.photokmm.data.utils

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


internal class LocalStorageManagerAndroid : FileSystemAccess {

    override suspend fun saveFile(fileName: String, data: ByteArray): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                // Get the directory for saving the file.
                val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!directory.exists()) {
                    directory.mkdirs()
                }

                // Create the file in the directory..
                val imageFile = File(directory, "$fileName.jpg")
                var outputStream: OutputStream? = null

                // Write data to the file we just created.
                try {
                    outputStream = FileOutputStream(imageFile)
                    outputStream.write(data)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                } finally {
                    outputStream?.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

actual fun provideFileSystemAccess(): FileSystemAccess = LocalStorageManagerAndroid()
