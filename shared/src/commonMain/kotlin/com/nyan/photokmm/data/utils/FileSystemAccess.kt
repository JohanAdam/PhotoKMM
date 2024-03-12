package com.nyan.photokmm.data.utils

interface FileSystemAccess {
    /**
     * Saves the provided data to a file on the external storage of the Android device.
     *
     * @param fileName The name of the file to be saved.
     * @param data The data to be written to the file.
     * @return True if the file is successfully saved, false otherwise.
     */
    suspend fun saveFile(fileName: String, data: ByteArray): Boolean
}

expect fun provideFileSystemAccess(): FileSystemAccess
