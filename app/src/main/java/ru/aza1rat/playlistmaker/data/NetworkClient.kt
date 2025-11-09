package ru.aza1rat.playlistmaker.data

import ru.aza1rat.playlistmaker.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}