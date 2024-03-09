package com.nyan.photokmm.data.remote.service

import com.nyan.photokmm.data.remote.ApiClient
import com.nyan.photokmm.data.remote.model.PhotosResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes

internal class PhotoService: ApiClient() {

    suspend fun getPhotos(tags: String, page: Int): PhotosResponse = client.get {
        pathUrl("services/rest")
        parameter("method", METHOD)
        parameter("tags", tags)
        parameter("format", FORMAT)
        parameter("nojsoncallback", NO_JSON_CALLBACK)
        parameter("extras", "$EXTRAS_MEDIA,$EXTRAS_URL_SQ,$EXTRAS_URL_M")
        parameter("per_page", MAX_ITEM_PER_PAGE)
        parameter("page", page)
    }.body()

    suspend fun downloadImage(imageUrl: String): ByteArray? {
        return try {
            val response: HttpResponse = client.get(imageUrl)
            //If success response code, return the bytes array.
            return if (response.status.value in 200..299) {
                val bytes = response.readBytes()
                bytes
            } else {
                // Handle non-2xx response codes (e.g., log error).
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val METHOD = "flickr.photos.search"
        private const val FORMAT = "json"
        private const val NO_JSON_CALLBACK = true
        private const val EXTRAS_MEDIA = "media"
        private const val EXTRAS_URL_SQ = "url_sq"
        private const val EXTRAS_URL_M = "url_m"
        private const val MAX_ITEM_PER_PAGE = 21
    }
}