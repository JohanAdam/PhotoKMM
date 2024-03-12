package com.nyan.photokmm.domain.usecase

import com.nyan.photokmm.data.utils.FileSystemAccess
import com.nyan.photokmm.domain.repository.PhotoRepository
import org.koin.core.component.KoinComponent

class DownloadImageUseCase(
    private val fileSystemAccess: FileSystemAccess,
    private val repository: PhotoRepository,
): KoinComponent {
    @Throws(Exception::class)
    suspend operator fun invoke(photoTitle: String, imageUrl: String): String? {
        //Download image into byte.
        val imageBytes = repository.downloadImage(imageUrl) ?: return null

        //Save the byte as image file in storage.
        return if (fileSystemAccess.saveFile(photoTitle, imageBytes)) {
            "Downloaded photo $photoTitle"
        } else {
            "Error while download image."
        }
    }
}