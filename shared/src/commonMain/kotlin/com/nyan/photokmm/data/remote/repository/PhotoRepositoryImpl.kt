package com.nyan.photokmm.data.remote.repository

import com.nyan.photokmm.data.remote.RemoteDataSource
import com.nyan.photokmm.data.utils.toPhoto
import com.nyan.photokmm.domain.model.Photo
import com.nyan.photokmm.domain.repository.PhotoRepository

internal class PhotoRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): PhotoRepository {
    override suspend fun getPhotos(tags: String): List<Photo> {
        return remoteDataSource.getPhotos(tags).photos.photo.map {
            it.toPhoto()
        }
    }
}