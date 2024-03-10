package com.nyan.photokmm.domain.usecase

import com.nyan.photokmm.domain.model.Photo
import com.nyan.photokmm.domain.repository.PhotoRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPhotosUseCase: KoinComponent {
    private val repository: PhotoRepository by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(tags: String): List<Photo> {
        return repository.getPhotos(tags)
    }
}