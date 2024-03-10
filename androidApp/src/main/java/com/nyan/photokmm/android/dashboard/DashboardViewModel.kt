package com.nyan.photokmm.android.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyan.photokmm.domain.model.Photo
import com.nyan.photokmm.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashboardViewModel(
    val getPhotosUseCase: GetPhotosUseCase
): ViewModel() {
    // Define the mutable state for the UI.
    var uiState by mutableStateOf(DashboardScreenState())
    private var currentPage = 1

    var message by mutableStateOf<String?>(null)

    //Search job delay.
    private var debounceJob: Job? = null
    var searchQuery by mutableStateOf("")
        private set

    init {
        //Load photos for the first time when the viewmodel is initialized.
        loadPhotos(forceReload = false)
    }

    fun loadPhotos(forceReload: Boolean = false) {
        // If already loading, return.
        if (uiState.loading) return
        // Reset current page if force reload is enabled.
        if (forceReload) currentPage = 1
        // Set refreshing state if loading first page.
        if (currentPage == 1) uiState = uiState.copy(refreshing = true)

        viewModelScope.launch {
            // Set loading state
            uiState = uiState.copy(loading = true)

            try {
                //Get the photos from source.
                val result = getPhotosUseCase(
                    tags = searchQuery.ifEmpty { "Electrolux" },
                    page = currentPage)
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
                // Handle error if anything happen during fetch photos.
                error.printStackTrace()

                //Update UI with error message.
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = true,
                    errorMsg = "Unable to load photos due to ${error.localizedMessage}"
                )
            }
        }
    }

    fun downloadSelectedImage(photoId: String) {
        message = "Downloaded photo $photoId"
    }

    fun onSearchTextChanged(searchText: String) {
        searchQuery = searchText

        //If searchText is not empty, we delay some input before submit to api.
        if (searchText.isNotEmpty()) {
            debounceJob?.cancel() // Cancel the previous debounce job.
            debounceJob = viewModelScope.launch {
                // Delay time 1 second before sent the query to API.
                delay(1000)
                loadPhotos(true)
            }
        } else {
            //If searchText is empty, we immediately call the api.
            loadPhotos(true)
        }
    }
}

data class DashboardScreenState (
    var loading: Boolean = false,
    var refreshing: Boolean = false,
    var photos: List<Photo> = listOf(),
    var errorMsg: String? = null,
    var loadFinished: Boolean = false,
)