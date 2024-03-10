package com.nyan.photokmm.android.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nyan.photokmm.android.Purple
import com.nyan.photokmm.android.common.image.AsyncImage
import com.nyan.photokmm.domain.model.Photo

@Composable
fun PhotoListItem(
    modifier: Modifier,
    photo: Photo,
    isSelected: Boolean,
    onPhotoClick: (Photo) -> Unit
) {
    val parentItemRadius = 18.dp

    Box(modifier = modifier.height(260.dp)) {
        Card(modifier = modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(parentItemRadius))
            .clickable { onPhotoClick(photo) }) {

            Column {
                // ==========================
                // Top Image Section.
                // ==========================
                AsyncImage(
                    url = photo.url,
                    contentDescription = photo.title,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .weight(1f)
                )

                // ==========================
                // Bottom Title Section.
                // ==========================
                Text(
                    modifier = modifier.padding(10.dp),
                    text = photo.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Overlay highlight with the same shape as the card
        if (isSelected) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(parentItemRadius)) // Clip the overlay highlight
                .background(Purple.copy(alpha = 0.4f)))
        }
    }
}