package com.nyan.photokmm.data.utils

interface FileSystemAccess {
    suspend fun saveFile(fileName: String, data: ByteArray): Boolean
    suspend fun getFile(fileName: String): ByteArray?
}
expect fun provideFileSystemAccess(): FileSystemAccess