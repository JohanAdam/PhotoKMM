package com.nyan.photokmm.domain.repository

import com.nyan.photokmm.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(tags: String): List<Photo>
    suspend fun downloadImage(imageUrl: String): ByteArray?
}