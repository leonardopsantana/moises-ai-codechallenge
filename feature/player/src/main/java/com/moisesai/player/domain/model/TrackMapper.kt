package com.moisesai.player.domain.model

import com.moisesai.player.data.model.ListTrackResponse
import com.moisesai.player.data.model.TrackResponse

fun ListTrackResponse.toListTracks(): List<Track> =
    this.results.map { response ->
        response.toTrack()
    }

fun TrackResponse.toTrack(): Track = Track(
    wrapperType = wrapperType,
    kind = kind,
    artistId = artistId,
    collectionId = collectionId,
    trackId = trackId,
    artistName = artistName,
    collectionName = collectionName,
    trackName = trackName,
    collectionCensoredName = collectionCensoredName,
    trackCensoredName = trackCensoredName,
    artistViewUrl = artistViewUrl,
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
    discCount = discCount,
    discNumber = discNumber,
    trackCount = trackCount,
    trackNumber = trackNumber,
    trackTimeMillis = trackTimeMillis,
    country = country,
    currency = currency,
    primaryGenreName = primaryGenreName,
    contentAdvisoryRating = contentAdvisoryRating,
    shortDescription = shortDescription,
    longDescription = longDescription,
    hasITunesExtras = hasITunesExtras,
    isStreamable = isStreamable
)

