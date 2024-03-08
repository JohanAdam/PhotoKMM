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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nyan.photokmm.android.Purple
import com.nyan.photokmm.domain.model.Photo

@Composable
fun PhotoListItem(
    modifier: Modifier,
    photo: Photo,
    isSelected: Boolean,
    onPhotoClick: (Photo) -> Unit
) {
    Box(
        modifier = modifier.height(260.dp)
    ) {
        Card(modifier = modifier
            .clickable { onPhotoClick.invoke(photo) }) {

            Column {
                // ==========================
                // Top Image Section.
                // ==========================
                Box(
                    modifier = modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = photo.url,
                        contentDescription = photo.title,
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .fillMaxSize()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 2.dp,
                                    bottomEnd = 2.dp
                                )
                            )
                    )
                }

                // ==========================
                // Bottom Title Section.
                // ==========================
                Column(
                    modifier = modifier.padding(10.dp)
                ) {
                    Text(
                        text = photo.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Overlay highlight with the same shape as the card
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Purple.copy(alpha = 0.3f))
                    .clip(shape = RoundedCornerShape(8.dp)) // Clip the overlay highlight
            )
        }
    }
}