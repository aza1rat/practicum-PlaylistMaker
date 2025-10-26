package ru.aza1rat.playlistmaker.data.dto

data class TrackResponse(
    val results: List<TrackDto>
) : BaseResponse()