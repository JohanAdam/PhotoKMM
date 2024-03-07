package com.nyan.photokmm.data.remote

import com.nyan.photokmm.data.remote.model.PhotosResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class PhotoService: ApiClient() {
    suspend fun getPhotos(tags: String): PhotosResponse = client.get {
        pathUrl("services/rest")
        parameter("method", "flickr.photos.search")
        parameter("tags", tags)
        parameter("format", "json")
        parameter("nojsoncallback", "true")
        parameter("extras", "media,url_sq,url_m")
        parameter("per_page", 20)
        parameter("page", 1)
    }.body()
}