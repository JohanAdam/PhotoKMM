package com.nyan.photokmm.data.utils

import com.nyan.photokmm.data.remote.model.PhotoDTO
import com.nyan.photokmm.domain.model.Photo

internal fun PhotoDTO.toPhoto(index: Int): Photo {
    return Photo(
        id = "$index-$id",
        title = title,
        url = url
    )
}