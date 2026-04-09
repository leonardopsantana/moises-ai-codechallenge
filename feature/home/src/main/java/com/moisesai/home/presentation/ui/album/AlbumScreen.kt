package com.moisesai.home.presentation.ui.album

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.moiseai.feature.home.R
import com.moisesai.core.ui.components.SongItemComponent
import com.moisesai.navigation.routes.Routes
import org.koin.androidx.compose.koinViewModel
import kotlin.collections.drop

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = koinViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (uiState) {
                is AlbumUIState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
                is AlbumUIState.Success -> {
                    val state = uiState as AlbumUIState.Success
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = CenterVertically
                    ) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                        Text(
                            text = "Album",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    SongItemComponent(
                        title = state.songs.first().collectionName ?: stringResource(R.string.unknown),
                        subtitle = state.songs.first().artistName ?:stringResource(R.string.unknown_artist),
                        artworkUrl = state.songs.first().artworkUrl100,
                        isHeader = true,
                        showNavOption = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(state.songs.drop(1)) { song ->
                            SongItemComponent(
                                title = song.trackName ?: stringResource(R.string.unknown),
                                subtitle = song.artistName ?: stringResource(R.string.unknown_artist),
                                artworkUrl = song.artworkUrl100,
                                showNavOption = false,
                                onClick = {
                                    song.trackId?.let { trackId ->
                                        val allTrackIds = state.songs.drop(1).mapNotNull { it.trackId }
                                        navController.navigate(Routes.playerRoute(allTrackIds, trackId))
                                    }
                                }
                            )
                        }
                    }
                }
                is AlbumUIState.Error -> {
                    Text(
                        text = stringResource(R.string.error_loading_album),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(CenterHorizontally)
                    )
                }
            }
        }
    }
}