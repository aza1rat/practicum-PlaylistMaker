package ru.aza1rat.playlistmaker.data.network

import ru.aza1rat.playlistmaker.data.NetworkClient
import ru.aza1rat.playlistmaker.data.dto.BaseResponse
import ru.aza1rat.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient (private val searchService: SearchAPI): NetworkClient {
    override fun doRequest(dto: Any): BaseResponse {
        if (dto is TrackSearchRequest) {
            try {
                val response = searchService.getSongs(dto.query).execute()
                val body = response.body() ?: BaseResponse()
                return body.apply { resultCode = response.code() }
            }
            catch (_: Exception) {
                return BaseResponse().apply { resultCode = 500 }
            }
        }
        else
            return BaseResponse().apply { resultCode = 400 }
    }
}