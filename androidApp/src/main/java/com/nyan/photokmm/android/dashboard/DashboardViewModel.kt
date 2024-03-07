package com.nyan.photokmm.android.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyan.photokmm.domain.model.Photo
import com.nyan.photokmm.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.launch

class DashboardViewModel(
    val getPhotosUseCase: GetPhotosUseCase
): ViewModel() {

    var uiState by mutableStateOf(DashboardScreenState())
    private var currentPage = 1

    init {
        loadPhotos(forceReload = false)
    }

    fun loadPhotos(forceReload: Boolean = false) {
        if (uiState.loading) return
        if (forceReload) currentPage = 1
        if (currentPage == 1) uiState = uiState.copy(refreshing = true)

        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            try {
                //Get the photos from source.
                val result = getPhotosUseCase(tags = "Electrolux")
                //Combine the result with the previous list.
                val photos = if (currentPage == 1) result else uiState.photos + result

                //Increment pages after success.
                currentPage += 1
                //Apply the result to ui state.
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = result.isEmpty(),
                    photos = photos
                )

            } catch (error: Throwable) {
                error.printStackTrace()

                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = true,
                    errorMsg = "Unable to load photos due to ${error.localizedMessage}"
                )
            }
        }
    }

}

data class DashboardScreenState (
    var loading: Boolean = false,
    var refreshing: Boolean = false,
    var photos: List<Photo> = listOf(),
    var errorMsg: String? = null,
    var loadFinished: Boolean = false
)