package com.nyan.photokmm.android.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyan.photokmm.android.Purple
import com.nyan.photokmm.domain.model.Photo
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardScreenState,
    loadNextPhotos: (Boolean) -> Unit,
    navigateToDetail: (Photo) -> Unit
) {
    //Pull to refresh state.
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.refreshing,
        onRefresh = { loadNextPhotos(true) }
    )

    // ==========================
    // Main content.
    // ==========================
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
                PhotoListItem(modifier = modifier,
                    photo = photo,
                    onPhotoClick = { navigateToDetail(photo) })

                //Load more photos if scrolled to the end.
                if (index >= uiState.photos.size - 1 && !uiState.loading && !uiState.loadFinished) {
                    LaunchedEffect(key1 = Unit, block = { loadNextPhotos(false) })
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
    }
}