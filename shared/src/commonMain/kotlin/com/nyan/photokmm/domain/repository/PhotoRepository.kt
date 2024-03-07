package com.nyan.photokmm.domain.repository

import com.nyan.photokmm.domain.model.Photo

internal interface PhotoRepository {
    suspend fun getPhotos(tags: String, page: Int): List<Photo>
}