package ru.aza1rat.playlistmaker.di

import org.koin.dsl.module
import ru.aza1rat.playlistmaker.playlist.domain.api.CopyFileToStorageUseCase
import ru.aza1rat.playlistmaker.playlist.domain.impl.CopyFileToStorageUseCaseImpl

val useCaseModule = module {
    factory<CopyFileToStorageUseCase> {
        CopyFileToStorageUseCaseImpl(get())
    }
}