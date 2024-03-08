package com.nyan.photokmm.android.dashboard

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.nyan.photokmm.android.Purple
import com.nyan.photokmm.android.R
import com.nyan.photokmm.android.common.textfield.SearchTextField
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

    var selectedPhotoId by remember { mutableStateOf<String?>(null) }

    //Pull to refresh state.
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.refreshing,
        onRefresh = { dashboardViewModel.loadPhotos(forceReload = true) }
    )

    // Observe the messageEvent and show a toast
    val messageEvent = dashboardViewModel.message
    if (messageEvent != null) {
        Toast.makeText(
            LocalContext.current,
            messageEvent,
            Toast.LENGTH_SHORT
        ).show()

        // Reset the event after showing the toast
        dashboardViewModel.message = null
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
            modifier = modifier
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
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ==========================
                // Photo List Item.
                // ==========================
                itemsIndexed(uiState.photos,
                    key = { _, photo -> photo.id }) { index, photo ->
                    val isSelected = photo.id == selectedPhotoId

                    PhotoListItem(
                        modifier = modifier,
                        photo = photo,
                        isSelected = isSelected,
                        onPhotoClick = {
                            //Update the selected photo Id.
                            selectedPhotoId = it.id
                        }
                    )

                    //Load more photos if scrolled to the end.
                    if (index >= uiState.photos.size - 1 && !uiState.loading && !uiState.loadFinished) {
                        LaunchedEffect(key1 = Unit, block = { dashboardViewModel.loadPhotos(false) })
                    }
                }

                // ==========================
                // Footer Progress Bar.
                // ==========================
                //Show footer progress bar IF fetch more photo is in progress and the list is not empty currently.
                if (uiState.loading && uiState.photos.isNotEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Row(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = Purple)
                        }
                    }
                }
            }

            // ==========================
            // Floating Action button.
            // ==========================
            // Show floating action button if a photo is selected
            if (selectedPhotoId != null) {
                FloatingActionButton(
                    onClick = {
                        dashboardViewModel.downloadSelectedImage(selectedPhotoId!!)
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