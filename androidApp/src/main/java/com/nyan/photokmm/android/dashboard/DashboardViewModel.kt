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

    //Search job delay.
    private var debounceJob: Job? = null
    var searchQuery by mutableStateOf("")
        private set

    //Marker for reset the scroll state, if the list is new.
    var isNewList = mutableStateOf(false)

    var message by mutableStateOf<String?>(null)

    init {
        //Load photos for the first time when the viewmodel is initialized.
        loadPhotos(forceReload = false)
    }

    fun loadPhotos(forceReload: Boolean = false) {
        // If already loading, return.
        if (uiState.loading) return
        // Set refreshing state true if loading first page OR we manually refresh.
        if (forceReload) uiState = uiState.copy(refreshing = true)

        viewModelScope.launch {
            // Set loading state
            uiState = uiState.copy(
                loading = true,
                photos = emptyList()
            )

            try {
                //Get the photos from source.
                val result = getPhotosUseCase(
                    tags = searchQuery.ifEmpty { "Electrolux" })

                //Apply the result to ui state.
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    photos = result
                )

            } catch (error: Throwable) {
                // Handle error if anything happen during fetch photos.
                error.printStackTrace()

                message = "Unable to load photos due to ${error.localizedMessage}"

                //Update UI with error message.
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                )
            }
        }
    }

    fun downloadSelectedImage(photo: Photo?) {
        message = "Downloaded photo ${photo?.title}"
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

        //Mark this as new list, so it can reset the list position.
        isNewList.value = true
    }
}

data class DashboardScreenState (
    var loading: Boolean = false,
    var refreshing: Boolean = false,
    var photos: List<Photo> = listOf(),
)