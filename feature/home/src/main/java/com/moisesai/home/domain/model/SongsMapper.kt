package com.moisesai.home.domain.model

import com.moisesai.home.data.model.ListSongsResponse
import com.moisesai.home.data.model.SongResponse

fun ListSongsResponse.toListSongs(): List<Song> =
    this.results.map { response ->
        response.toSong()
    }

fun SongResponse.toSong(): Song =
    Song(
        wrapperType = wrapperType,
        kind = kind,
        collectionId = collectionId,
        trackId = trackId,
        artistName = artistName,
        collectionName = collectionName,
        trackName = trackName,
        collectionCensoredName = collectionCensoredName,
        trackCensoredName = trackCensoredName,
        collectionArtistId = collectionArtistId,
        collectionArtistViewUrl = collectionArtistViewUrl,
        collectionViewUrl = collectionViewUrl,
        trackViewUrl = trackViewUrl,
        previewUrl = previewUrl,
        artworkUrl30 = artworkUrl30,
        artworkUrl60 = artworkUrl60,
        artworkUrl100 = artworkUrl100,
        collectionPrice = collectionPrice,
        trackPrice = trackPrice,
        trackRentalPrice = trackRentalPrice,
        collectionHdPrice = collectionHdPrice,
        trackHdPrice = trackHdPrice,
        trackHdRentalPrice = trackHdRentalPrice,
        releaseDate = releaseDate,
        collectionExplicitness = collectionExplicitness,
        trackExplicitness = trackExplicitness,
        trackCount = trackCount,
        trackNumber = trackNumber,
        trackTimeMillis = trackTimeMillis,
        country = country,
        currency = currency,
        primaryGenreName = primaryGenreName,
        contentAdvisoryRating = contentAdvisoryRating,
        shortDescription = shortDescription,
        longDescription = longDescription,
        hasITunesExtras = hasITunesExtras
    )

