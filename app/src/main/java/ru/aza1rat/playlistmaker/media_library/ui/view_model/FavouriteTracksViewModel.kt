package ru.aza1rat.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.media_library.domain.api.FavouritesInteractor
import ru.aza1rat.playlistmaker.media_library.ui.model.FavouritesState

class FavouriteTracksViewModel(private val favouritesInteractor: FavouritesInteractor) : ViewModel() {
    private val favouritesState = MutableLiveData<FavouritesState>()
    fun observeFavouritesState(): LiveData<FavouritesState> = favouritesState

    fun getFavourites() {
        viewModelScope.launch {
            favouritesInteractor.list().collect { favourites ->
                if (favourites.isNotEmpty()) {
                    favouritesState.value = FavouritesState.FavouritesContent(favourites)
                }
                else {
                    favouritesState.value = FavouritesState.Empty
                }
            }
        }
    }
}