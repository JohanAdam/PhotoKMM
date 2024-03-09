package com.nyan.photokmm.data.remote.repository

import com.nyan.photokmm.data.remote.RemoteDataSource
import com.nyan.photokmm.data.utils.toPhoto
import com.nyan.photokmm.domain.model.Photo
import com.nyan.photokmm.domain.repository.PhotoRepository

internal class PhotoRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
): PhotoRepository {
    override suspend fun getPhotos(tags: String, page: Int): List<Photo> {
        return remoteDataSource.getPhotos(tags, page).photos.photo.mapIndexed { index, photoDTO ->
            //We mapping the PhotoDTO to Photo before send the list to view.
            photoDTO.toPhoto(index)
        }
    }

    override suspend fun downloadImage(imageUrl: String): ByteArray? {
        return remoteDataSource.downloadImages(imageUrl)
    }
}