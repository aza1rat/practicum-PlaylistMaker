package ru.aza1rat.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.aza1rat.playlistmaker.data.NetworkClient
import ru.aza1rat.playlistmaker.data.dto.BaseResponse
import ru.aza1rat.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkClient: NetworkClient {
    private val retrofitClient = Retrofit.Builder()
        .baseUrl(ITUNES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchService: SearchAPI = retrofitClient.create(SearchAPI::class.java)

    override fun doRequest(dto: Any): BaseResponse {
        if (dto is TrackSearchRequest) {
            try {
                val response = searchService.getSongs(dto.query).execute()
                val body = response.body() ?: BaseResponse()
                return body.apply { resultCode = response.code() }
            }
            catch (exception: Exception) {
                return BaseResponse().apply { resultCode = 500 }
            }
        }
        else
            return BaseResponse().apply { resultCode = 400 }
    }
    companion object {
        private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    }
}