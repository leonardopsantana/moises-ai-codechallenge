package com.moisesai.player.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moisesai.navigation.routes.Routes
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import com.moiseai.feature.player.R
import com.moisesai.common.extensions.formatTime
import com.moisesai.core.ui.components.SongItemComponent
import com.moisesai.player.domain.model.Track
import androidx.compose.ui.platform.LocalConfiguration

private const val TABLET_DEFAULT_SIZE = 600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = koinViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistUiState by viewModel.playlistUiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= TABLET_DEFAULT_SIZE

    Scaffold { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            handlePlayerState(uiState, navController, viewModel)
            handlePlaylistState(playlistUiState, uiState, viewModel, isTablet)
        }
    }
}

@Composable
private fun RowScope.handlePlaylistState(
    playlistUiState: PlaylistUiState,
    uiState: PlayerUIState,
    viewModel: PlayerViewModel,
    isTablet: Boolean
) {
    when (playlistUiState) {
        is PlaylistUiState.Success -> {
            if (uiState is PlayerUIState.Success) {
                if(isTablet) {
                    Playlist(
                        playlistUiState.playableTracks,
                        uiState.currentTrack,
                        viewModel
                    )
                }
            } else {
                Loading()
            }
        }

        is PlaylistUiState.Loading -> {
            if(isTablet) {
                Loading()
            }
        }

        is PlaylistUiState.Error -> {
            Box(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error loading playlist",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun RowScope.handlePlayerState(
    uiState: PlayerUIState,
    navController: NavController,
    viewModel: PlayerViewModel
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    when (uiState) {
        is PlayerUIState.Error -> {
            Text(
                text = stringResource(R.string.error_loading_song),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(CenterVertically)
            )
        }

        is PlayerUIState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.align(CenterVertically)
            )
        }

        is PlayerUIState.Success -> {

            val sliderPosition =
                if (uiState.duration > 0) (uiState.currentPosition.toFloat() / uiState.duration) else 0f
            val remainingTime = uiState.duration - uiState.currentPosition
            Player(
                navController,
                uiState.currentTrack,
                viewModel,
                sliderPosition,
                uiState.duration,
                uiState.currentPosition,
                remainingTime,
                uiState.isPlaying,
                uiState.isRepeatOn
            )
        }
    }
}

@Composable
private fun RowScope.Loading() {
    Box(
        modifier = Modifier
            .weight(0.25f)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun RowScope.Player(
    navController: NavController,
    currentTrack: Track?,
    viewModel: PlayerViewModel,
    sliderPosition: Float,
    duration: Long,
    currentPosition: Long,
    remainingTime: Long,
    isPlaying: Boolean,
    isRepeatOn: Boolean
) {
    Column(
        modifier = Modifier
            .weight(0.75f)
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Header(navController, currentTrack)

        AlbumCover(viewModel)

        AlbumInfo(currentTrack)

        SongBar(sliderPosition, viewModel, duration, currentPosition, remainingTime)

        PlayerControls(viewModel, isPlaying, isRepeatOn)
    }
}

@Composable
private fun RowScope.Playlist(
    playableTracks: List<Track>,
    currentTrack: Track?,
    viewModel: PlayerViewModel
) {
    Column(
        modifier = Modifier
            .weight(0.20f)
            .padding(16.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_music_list),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(8.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(playableTracks) { track ->
                SongItemComponent(
                    title = track.trackName.orEmpty(),
                    subtitle = track.artistName.orEmpty(),
                    artworkUrl = track.artworkUrl100,
                    showNavOption = false,
                    isPlaying = track.trackId == currentTrack?.trackId,
                    onClick = {
                        viewModel.playTrack(track)
                    }
                )
            }
        }
    }
}

@Composable
private fun PlayerControls(
    viewModel: PlayerViewModel,
    isPlaying: Boolean,
    isRepeatOn: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { viewModel.togglePlayPause() },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(32.dp)
                )
                .size(64.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(
            onClick = { viewModel.previousTrack() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_forward_bar_fill),
                contentDescription = "Previous",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        IconButton(
            onClick = { viewModel.nextTrack() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_forward_bar_fill),
                contentDescription = stringResource(R.string.next),
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer(rotationZ = 180f),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { viewModel.toggleRepeat() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play_on_repeat),
                contentDescription = stringResource(R.string.repeat),
                modifier = Modifier.size(32.dp),
                tint = if (isRepeatOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun AlbumInfo(currentTrack: Track?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = currentTrack?.trackName.orEmpty(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentTrack?.artistName.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ColumnScope.AlbumCover(viewModel: PlayerViewModel) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .align(Alignment.CenterHorizontally)
            .clip(RoundedCornerShape(16.dp))
    ) {
        SubcomposeAsyncImage(
            model = viewModel.getArtworkUrl(),
            contentDescription = stringResource(R.string.album_cover),
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

@Composable
private fun Header(
    navController: NavController,
    currentTrack: Track?
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Now playing",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.size(40.dp))
        Row {
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
                            currentTrack?.collectionId?.let { collectionId ->
                                navController.navigate("${Routes.ALBUM_DETAIL}/$collectionId")
                            }
                            isMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SongBar(
    sliderPosition: Float,
    viewModel: PlayerViewModel,
    duration: Long,
    currentPosition: Long,
    remainingTime: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = { newPosition ->
                viewModel.seekTo((newPosition * duration).toLong())
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..1f,
            thumb = {
                Image(
                    painter = painterResource(id = R.drawable.scrubber),
                    contentDescription = "Slider Scrubber",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Fit
                )
            },
            track = { sliderState ->
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = (-2).dp)
                        .height(4.dp)
                ) {
                    val trackWidth = size.width + 11
                    val activeTrackWidth =
                        sliderState.value * trackWidth / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)

                    drawRect(
                        color = Color(0xFFFFFFFF).copy(alpha = 0.25f),
                        topLeft = Offset(activeTrackWidth, 0f),
                        size = Size(trackWidth - activeTrackWidth, size.height)
                    )

                    drawRect(
                        color = Color(0xFFFFFFFF).copy(alpha = 0.6f),
                        topLeft = Offset(0f, 0f),
                        size = Size(activeTrackWidth, size.height)
                    )
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentPosition.formatTime(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Text(
                text = "-${remainingTime.formatTime()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

