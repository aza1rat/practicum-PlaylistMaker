package ru.aza1rat.playlistmaker.history.data

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
    fun clearData()
}