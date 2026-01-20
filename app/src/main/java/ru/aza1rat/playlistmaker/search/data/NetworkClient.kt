package ru.aza1rat.playlistmaker.search.data

import ru.aza1rat.playlistmaker.search.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}