package com.nyan.photokmm.domain.usecase

import com.nyan.photokmm.data.utils.FileSystemAccess
import com.nyan.photokmm.domain.repository.PhotoRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DownloadImageUseCase: KoinComponent {
    private val repository: PhotoRepository by inject()
    private val fileSystemAccess: FileSystemAccess by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(imageUrl: String): String? {
        val imageBytes = repository.downloadImage(imageUrl) ?: return null

        val fileName = "image_test.jpg"
        if (fileSystemAccess.saveFile(fileName, imageBytes)) {
            return fileName
        }
        return null
    }
}