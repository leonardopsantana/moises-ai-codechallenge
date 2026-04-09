package com.moisesai.home.album

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.moisesai.home.InstantTaskCoroutinesExecutorRule
import com.moisesai.home.domain.model.Song
import com.moisesai.home.domain.usecase.GetSongsByAlbumUseCase
import com.moisesai.home.presentation.ui.album.AlbumUIState
import com.moisesai.home.presentation.ui.album.AlbumViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AlbumViewModelTest {
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var getSongsByAlbumUseCase: GetSongsByAlbumUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var executorRule = InstantTaskCoroutinesExecutorRule()

    private val mockAlbumSongs = listOf(
        // Album header (usually album info)
        Song(
            wrapperType = "collection",
            kind = "collection",
            collectionId = 123L,
            trackId = null,
            artistName = "Artist 1",
            collectionName = "Album 1",
            trackName = "Album 1",
            collectionCensoredName = "Album 1",
            trackCensoredName = "Album 1",
            collectionArtistId = 456L,
            collectionArtistViewUrl = "http://example.com",
            collectionViewUrl = "http://example.com",
            trackViewUrl = null,
            previewUrl = null,
            artworkUrl30 = "http://art30.com",
            artworkUrl60 = "http://art60.com",
            artworkUrl100 = "http://art100.com",
            collectionPrice = 9.99,
            trackPrice = null,
            trackRentalPrice = null,
            collectionHdPrice = 0.0,
            trackHdPrice = null,
            trackHdRentalPrice = null,
            releaseDate = "2023-01-01",
            collectionExplicitness = "notExplicit",
            trackExplicitness = null,
            trackCount = 2,
            trackNumber = null,
            trackTimeMillis = null,
            country = "US",
            currency = "USD",
            primaryGenreName = "Pop",
            contentAdvisoryRating = null,
            shortDescription = "Pop Album",
            longDescription = "A wonderful pop album",
            hasITunesExtras = false
        ),
        // Track 1
        Song(
            wrapperType = "track",
            kind = "song",
            collectionId = 123L,
            trackId = 1L,
            artistName = "Artist 1",
            collectionName = "Album 1",
            trackName = "Track 1",
            collectionCensoredName = "Album 1",
            trackCensoredName = "Track 1",
            collectionArtistId = 456L,
            collectionArtistViewUrl = "http://example.com",
            collectionViewUrl = "http://example.com",
            trackViewUrl = "http://example.com",
            previewUrl = "http://preview.com",
            artworkUrl30 = "http://art30.com",
            artworkUrl60 = "http://art60.com",
            artworkUrl100 = "http://art100.com",
            collectionPrice = 9.99,
            trackPrice = 1.29,
            trackRentalPrice = 0.0,
            collectionHdPrice = 0.0,
            trackHdPrice = 0.0,
            trackHdRentalPrice = 0.0,
            releaseDate = "2023-01-01",
            collectionExplicitness = "notExplicit",
            trackExplicitness = "notExplicit",
            trackCount = 2,
            trackNumber = 1,
            trackTimeMillis = 180000,
            country = "US",
            currency = "USD",
            primaryGenreName = "Pop",
            contentAdvisoryRating = "clean",
            shortDescription = "Track 1",
            longDescription = "First track of the album",
            hasITunesExtras = false
        ),
        // Track 2
        Song(
            wrapperType = "track",
            kind = "song",
            collectionId = 123L,
            trackId = 2L,
            artistName = "Artist 1",
            collectionName = "Album 1",
            trackName = "Track 2",
            collectionCensoredName = "Album 1",
            trackCensoredName = "Track 2",
            collectionArtistId = 456L,
            collectionArtistViewUrl = "http://example.com",
            collectionViewUrl = "http://example.com",
            trackViewUrl = "http://example.com",
            previewUrl = "http://preview.com",
            artworkUrl30 = "http://art30.com",
            artworkUrl60 = "http://art60.com",
            artworkUrl100 = "http://art100.com",
            collectionPrice = 9.99,
            trackPrice = 1.29,
            trackRentalPrice = 0.0,
            collectionHdPrice = 0.0,
            trackHdPrice = 0.0,
            trackHdRentalPrice = 0.0,
            releaseDate = "2023-01-01",
            collectionExplicitness = "notExplicit",
            trackExplicitness = "notExplicit",
            trackCount = 2,
            trackNumber = 2,
            trackTimeMillis = 200000,
            country = "US",
            currency = "USD",
            primaryGenreName = "Pop",
            contentAdvisoryRating = "clean",
            shortDescription = "Track 2",
            longDescription = "Second track of the album",
            hasITunesExtras = false
        )
    )

    @Before
    fun setUp() {
        getSongsByAlbumUseCase = mockk()
        savedStateHandle = SavedStateHandle(mapOf("albumId" to "123"))
        coEvery { getSongsByAlbumUseCase("123") } returns flowOf(mockAlbumSongs)
        albumViewModel = AlbumViewModel(
            getSongsByAlbumUseCase = getSongsByAlbumUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `should initialize with Loading state and load album songs on init`() {
        runTest {
            val state = albumViewModel.uiState.value
            assertIs<AlbumUIState.Success>(state)
            assertEquals(2, state.songs.size)  // First song (header) is dropped
            assertEquals("Track 1", state.songs[0].trackName)
            assertEquals("Track 2", state.songs[1].trackName)
        }
    }

    @Test
    fun `should successfully load songs and drop first song (album header)`() {
        runTest {
            val state = albumViewModel.uiState.value
            assertIs<AlbumUIState.Success>(state)

            // Verify that the first song (collection item) is dropped
            assertEquals(2, state.songs.size)
            assertEquals(1L, state.songs[0].trackId)  // Track 1
            assertEquals(2L, state.songs[1].trackId)  // Track 2
        }
    }

    @Test
    fun `should set error state when GetSongsByAlbumUseCase throws exception`() {
        runTest {
            coEvery { getSongsByAlbumUseCase("123") } throws Exception("Network error")
            val viewModel = AlbumViewModel(getSongsByAlbumUseCase, savedStateHandle)

            val state = viewModel.uiState.value
            assertIs<AlbumUIState.Error>(state)
        }
    }

    @Test
    fun `should set error state when album songs list is empty`() {
        runTest {
            coEvery { getSongsByAlbumUseCase("456") } returns flowOf(emptyList())
            val emptyStateHandle = SavedStateHandle(mapOf("albumId" to "456"))
            val viewModel = AlbumViewModel(getSongsByAlbumUseCase, emptyStateHandle)

            val state = viewModel.uiState.value
            assertIs<AlbumUIState.Error>(state)
        }
    }

    @Test
    fun `should call GetSongsByAlbumUseCase with correct albumId`() {
        runTest {
            coEvery { getSongsByAlbumUseCase("123") } returns flowOf(mockAlbumSongs)
            val viewModel = AlbumViewModel(getSongsByAlbumUseCase, savedStateHandle)

            coVerify { getSongsByAlbumUseCase("123") }
        }
    }

    @Test
    fun `should handle null albumId from SavedStateHandle`() {
        runTest {
            val emptyStateHandle = SavedStateHandle()
            coEvery { getSongsByAlbumUseCase("") } returns flowOf(mockAlbumSongs)
            val viewModel = AlbumViewModel(getSongsByAlbumUseCase, emptyStateHandle)

            coVerify { getSongsByAlbumUseCase("") }
        }
    }

    @Test
    fun `should retain only album tracks when first item is collection`() {
        runTest {
            val state = albumViewModel.uiState.value
            assertIs<AlbumUIState.Success>(state)

            // All remaining items should be tracks (not collection)
            state.songs.forEach { song ->
                assertEquals("track", song.wrapperType)
                assertEquals("song", song.kind)
            }
        }
    }

    @Test
    fun `should successfully load large song list and drop first item`() {
        runTest {
            val largeSongList = mockAlbumSongs + mockAlbumSongs + mockAlbumSongs
            coEvery { getSongsByAlbumUseCase("999") } returns flowOf(largeSongList)
            val largeStateHandle = SavedStateHandle(mapOf("albumId" to "999"))
            val viewModel = AlbumViewModel(getSongsByAlbumUseCase, largeStateHandle)

            val state = viewModel.uiState.value
            assertIs<AlbumUIState.Success>(state)
            assertEquals(8, state.songs.size)  // 9 - 1 (dropped header)
        }
    }

    @Test
    fun `should preserve song properties after dropping header`() {
        runTest {
            val state = albumViewModel.uiState.value
            assertIs<AlbumUIState.Success>(state)

            val firstTrack = state.songs[0]
            assertEquals("Track 1", firstTrack.trackName)
            assertEquals("Artist 1", firstTrack.artistName)
            assertEquals("Album 1", firstTrack.collectionName)
            assertEquals(1L, firstTrack.trackId)
            assertEquals(123L, firstTrack.collectionId)
        }
    }
}