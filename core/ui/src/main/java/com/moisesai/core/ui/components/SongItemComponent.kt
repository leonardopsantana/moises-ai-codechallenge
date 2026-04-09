package com.moisesai.core.ui.components

import androidx.compose.runtime.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.CircularProgressIndicator
import coil.compose.SubcomposeAsyncImage
import com.moisesai.core.ui.R
import com.moisesai.core.ui.theme.ThemePreviews

@Composable
fun SongItemComponent(
    title: String,
    subtitle: String,
    artworkUrl: String?,
    modifier: Modifier = Modifier,
    isHeader: Boolean = false,
    showNavOption: Boolean = true,
    isPlaying: Boolean = false,
    onMenuClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isHeader) { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val albumArtSize = if (isHeader) 100.dp else 50.dp
        Box(
            modifier = Modifier
                .size(albumArtSize)
                .clip(RoundedCornerShape(if (isHeader) 8.dp else 4.dp))
        ) {
            if (!artworkUrl.isNullOrEmpty()) {
                SubcomposeAsyncImage(
                    model = artworkUrl,
                    contentDescription = "Album Art",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    },
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                maxLines = 1
            )
        }

        if(showNavOption){
            Box {
                IconButton(
                    onClick = { isMenuExpanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "View album",
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = "Chevron",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        onClick = {
                            onMenuClick()
                            isMenuExpanded = false
                        }
                    )
                }
            }
        }
        if (isPlaying) {
            Image(
                painter = painterResource(id = R.drawable.playing_animation_icon),
                contentDescription = "Playing",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@ThemePreviews
@Composable
fun SongItemComponentPreview() {
    com.moisesai.core.ui.theme.ThemePreview {
        Column {
            SongItemComponent(
                title = "Bohemian Rhapsody",
                subtitle = "Queen",
                artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/42/20/88/422088ba-d965-7eaa-cd51-e10a4a77e74e/00028948099126.jpg/500x500bb.jpg",
                isPlaying = false
            )

            SongItemComponent(
                title = "Stairway to Heaven",
                subtitle = "Led Zeppelin",
                artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/f3/2f/6c/f32f6c2e-d965-7eaa-cd51-e10a4a77e74e/00008902099126.jpg/500x500bb.jpg",
                isPlaying = true
            )

            SongItemComponent(
                title = "Album Header",
                subtitle = "Artist Name",
                artworkUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/f3/2f/6c/f32f6c2e-d965-7eaa-cd51-e10a4a77e74e/00008902099126.jpg/500x500bb.jpg",
                isHeader = true,
                showNavOption = false
            )
        }
    }
}
