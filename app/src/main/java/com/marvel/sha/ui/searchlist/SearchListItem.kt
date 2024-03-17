package com.marvel.sha.ui.searchlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
internal fun <T : Any> SearchListItem(
    it: T,
    badge: Any? = null,
    onClick: (T) -> Unit,
    imageUrl: (T) -> String,
    caption: (T) -> String,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .clickable { onClick(it) }
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    model = imageUrl(it),
                    contentDescription = "Thumbnail",
                )
                if (badge != null) Badge(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp),
                ) { Text(text = badge.toString()) }
            }
            Spacer(
                modifier = Modifier.width(16.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = caption(it)
            )
        }
    }
}
