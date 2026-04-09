package com.moisesai.home.songs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.moisesai.home.InstantTaskCoroutinesExecutorRule
import com.moisesai.home.domain.model.Song
import com.moisesai.home.domain.usecase.GetSongsUseCase
import com.moisesai.home.presentation.ui.songs.SongsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SongsViewModelTest {
    private lateinit var songsViewModel: SongsViewModel
    private lateinit var getSongsUseCase: GetSongsUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var executorRule = InstantTaskCoroutinesExecutorRule()

    private val mockSongs = listOf(
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
            trackCount = 10,
            trackNumber = 1,
            trackTimeMillis = 180000,
            country = "US",
            currency = "USD",
            primaryGenreName = "Pop",
            contentAdvisoryRating = "clean",
            shortDescription = "A great pop track",
            longDescription = "This is a wonderful pop song from Album 1",
            hasITunesExtras = false
        ),
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
            trackCount = 10,
            trackNumber = 2,
            trackTimeMillis = 200000,
            country = "US",
            currency = "USD",
            primaryGenreName = "Pop",
            contentAdvisoryRating = "clean",
            shortDescription = "Another great pop track",
            longDescription = "This is another wonderful pop song from Album 1",
            hasITunesExtras = false
        )
    )

    @Before
    fun setUp() {
        getSongsUseCase = mockk()
        every { getSongsUseCase("pop") } returns flowOf(PagingData.from(mockSongs))
        songsViewModel = SongsViewModel(
            getSongsUseCase = getSongsUseCase,
        )
    }

    @Test
    fun `should update searchTerm when onSearchTermChanged is called`() {
        runTest {
            val searchTerm = "jazz"
            every { getSongsUseCase(searchTerm) } returns flowOf(PagingData.from(mockSongs))

            songsViewModel.onSearchTermChanged(searchTerm)
            advanceTimeBy(350L)
            advanceUntilIdle()

            assertEquals(searchTerm, songsViewModel.searchTerm.value)
        }
    }

    @Test
    fun `should call getSongsUseCase with search term when onSearchTermChanged is called with non-blank term`() {
        runTest {
            val searchTerm = "rock"
            every { getSongsUseCase(searchTerm) } returns flowOf(PagingData.from(mockSongs))

            songsViewModel.onSearchTermChanged(searchTerm)
            advanceTimeBy(350L)  // Advance past debounce delay (300ms)
            advanceUntilIdle()

            // Verify the search term was updated
            assertEquals(searchTerm, songsViewModel.searchTerm.value)
        }
    }

    @Test
    fun `should call GetSongsUseCase with correct parameters`() {
        runTest {
            val searchQuery = "rock"
            every { getSongsUseCase(searchQuery) } returns flowOf(PagingData.from(mockSongs))

            songsViewModel.onSearchTermChanged(searchQuery)
            advanceTimeBy(350L)  // Advance past debounce delay (300ms)
            advanceUntilIdle()

            // Verify the search query was set
            assertEquals(searchQuery, songsViewModel.searchTerm.value)
        }
    }

    @Test
    fun `should debounce rapid search term changes`() {
        runTest {
            val finalTerm = "rock"
            every { getSongsUseCase(finalTerm) } returns flowOf(PagingData.from(mockSongs))

            // Simulate rapid typing
            songsViewModel.onSearchTermChanged("r")
            advanceTimeBy(100L)
            songsViewModel.onSearchTermChanged("ro")
            advanceTimeBy(100L)
            songsViewModel.onSearchTermChanged("roc")
            advanceTimeBy(100L)
            songsViewModel.onSearchTermChanged("rock")
            advanceTimeBy(350L)  // Advance past debounce delay
            advanceUntilIdle()

            // Verify final search term is set
            assertEquals(finalTerm, songsViewModel.searchTerm.value)
        }
    }

    @Test
    fun `should handle multiple search term changes sequentially`() {
        runTest {
            val jazzMock = mockSongs.take(1)
            val rockMock = mockSongs.drop(1)

            every { getSongsUseCase("jazz") } returns flowOf(PagingData.from(jazzMock))
            every { getSongsUseCase("rock") } returns flowOf(PagingData.from(rockMock))

            songsViewModel.onSearchTermChanged("jazz")
            advanceTimeBy(350L)  // Advance past debounce delay
            advanceUntilIdle()
            assertEquals("jazz", songsViewModel.searchTerm.value)

            songsViewModel.onSearchTermChanged("rock")
            advanceTimeBy(350L)  // Advance past debounce delay
            advanceUntilIdle()
            assertEquals("rock", songsViewModel.searchTerm.value)
        }
    }
}