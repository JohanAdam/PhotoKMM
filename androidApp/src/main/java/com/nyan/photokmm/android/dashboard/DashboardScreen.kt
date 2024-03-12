package com.nyan.photokmm.android.dashboard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nyan.photokmm.android.R
import com.nyan.photokmm.android.common.textfield.SearchTextField
import com.nyan.photokmm.domain.model.Photo
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    val dashboardViewModel: DashboardViewModel = koinViewModel()
    val uiState = dashboardViewModel.uiState

    var selectedPhoto by remember { mutableStateOf<Photo?>(null) }

    val listState = rememberLazyGridState()

    //Pull to refresh state.
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.refreshing,
        onRefresh = { dashboardViewModel.loadPhotos(forceReload = true) }
    )

    // Observe the messageEvent and show a toast.
    val message = dashboardViewModel.message
    if (message != null) {
        ShowToast(message)

        // Reset the event after showing the toast.
        dashboardViewModel.message = null
    }

    //Reset selectedPhoto when refresh new list.
    val isNewList = uiState.refreshing
    LaunchedEffect(isNewList) {
        selectedPhoto = null
    }

    // ==========================
    // Main content.
    // ==========================
    Column {
        // ==========================
        // Search Field.
        // ==========================
        SearchTextField(
            value = dashboardViewModel.searchQuery,
            onValueChange = dashboardViewModel::onSearchTextChanged,
            onImeAction = { },
            isRefreshing = uiState.refreshing,
            modifier = modifier,
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .pullRefresh(state = pullRefreshState)
        ) {
            // ==========================
            // Pull to Refresh Indicator.
            // ==========================
            PullRefreshIndicator(
                refreshing = uiState.refreshing,
                state = pullRefreshState,
                modifier = modifier.align(Alignment.TopCenter)
            )

            // ==========================
            // List.
            // ==========================
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================
                // Photo List Item.
                // ==========================
                itemsIndexed(uiState.photos,
                    key = { _, photo -> photo.id }) { _, photo ->
                    val isSelected = photo.id == selectedPhoto?.id

                    PhotoListItem(
                        modifier = modifier,
                        photo = photo,
                        isSelected = isSelected,
                        onPhotoClick = {
                            //Update the selected photo Id.
                            selectedPhoto = if (selectedPhoto?.id != it.id) {
                                it
                            } else {
                                null
                            }
                        }
                    )
                }
            }

            // ==========================
            // Floating Action button.
            // ==========================
            // Show floating action button if a photo is selected
            if (selectedPhoto != null) {
                FloatingActionButton(
                    onClick = {
                        dashboardViewModel.downloadSelectedImage(selectedPhoto)
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "Download"
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowToast(errorMsg: String?) {
    Toast.makeText(
        LocalContext.current,
        errorMsg,
        Toast.LENGTH_SHORT
    ).show()
}