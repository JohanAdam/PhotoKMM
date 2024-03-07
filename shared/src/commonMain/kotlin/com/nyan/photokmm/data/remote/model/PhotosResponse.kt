package com.nyan.photokmm.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PhotosResponse(
    val photos: PhotosDTO,
    val stat: String
)

@Serializable
internal data class PhotosDTO(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoDTO>
)

@Serializable
internal data class PhotoDTO(
    val id: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int,
    @SerialName("url_m")
    val url: String,
    @SerialName("height_m")
    val height: Int,
    @SerialName("width_m")
    val width: Int
)