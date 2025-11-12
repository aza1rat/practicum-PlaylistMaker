package ru.aza1rat.playlistmaker.util.data

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(data: T? = null): Resource<T>(data)
}