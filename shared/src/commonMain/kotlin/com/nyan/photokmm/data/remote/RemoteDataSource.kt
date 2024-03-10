package com.nyan.photokmm.data.remote

import com.nyan.photokmm.data.remote.service.PhotoService
import com.nyan.photokmm.data.utils.Dispatcher
import kotlinx.coroutines.withContext

internal class RemoteDataSource(
    private val apiService: PhotoService,
    private val dispatcher: Dispatcher
) {

    suspend fun getPhotos(tags: String) = withContext(dispatcher.io) {
        apiService.getPhotos(tags = tags)
    }

}