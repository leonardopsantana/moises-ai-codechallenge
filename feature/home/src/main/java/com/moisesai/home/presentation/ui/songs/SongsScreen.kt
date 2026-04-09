package com.moisesai.home.presentation.ui.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.moiseai.feature.home.R
import com.moisesai.core.ui.components.SongItemComponent
import com.moisesai.navigation.routes.Routes
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: SongsViewModel = koinViewModel(),
    navController: NavController,
) {
    val searchTerm by viewModel.searchTerm.collectAsStateWithLifecycle()
    var searchTextFieldValue by remember { mutableStateOf(TextFieldValue(searchTerm)) }
    val pagingDataFlow by viewModel.songsPagingData.collectAsStateWithLifecycle()
    val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.songs),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchTextFieldValue,
                onValueChange = { newValue ->
                    searchTextFieldValue = newValue
                    viewModel.onSearchTermChanged(newValue.text)
                },
                placeholder = {
                    Text(
                        stringResource(R.string.search_your_library),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (lazyPagingItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is LoadState.Error -> {
                        Text(
                            text = "Error loading songs",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(lazyPagingItems.itemCount) { index ->
                                lazyPagingItems[index]?.let { song ->
                                    SongItemComponent(
                                        title = song.trackName ?: "Unknown",
                                        subtitle = song.artistName ?: "Unknown Artist",
                                        artworkUrl = song.artworkUrl100,
                                        onMenuClick = {
                                            song.collectionId?.let { collectionId ->
                                                navController.navigate("${Routes.ALBUM_DETAIL}/$collectionId")
                                            }
                                        },
                                        onClick = {
                                            val allTrackIds = (0 until minOf(lazyPagingItems.itemCount, 15))
                                                .mapNotNull { i -> lazyPagingItems[i]?.trackId }
                                            val currentTrackId = song.trackId ?: 0
                                            navController.navigate(Routes.playerRoute(allTrackIds, currentTrackId))
                                        }
                                    )
                                }
                            }

                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }

                                is LoadState.Error -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Error loading more",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

