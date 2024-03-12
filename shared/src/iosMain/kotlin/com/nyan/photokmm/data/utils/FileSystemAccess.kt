package com.nyan.photokmm.data.utils

internal class LocalStorageManagerIos: FileSystemAccess {
    override suspend fun saveFile(fileName: String, data: ByteArray): Boolean {
        throw NotImplementedError()
    }

}

actual fun provideFileSystemAccess(): FileSystemAccess = LocalStorageManagerIos()

