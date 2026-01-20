package ru.aza1rat.playlistmaker.search.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.aza1rat.playlistmaker.search.data.dto.BaseResponse
import ru.aza1rat.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient (private val searchService: SearchAPI): NetworkClient {
    override suspend fun doRequest(dto: Any): BaseResponse {
        if (dto is TrackSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = searchService.getSongs(dto.query)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    BaseResponse().apply { resultCode = 500 }
                }
            }
        }
        else
            return BaseResponse().apply { resultCode = 400 }
    }
}