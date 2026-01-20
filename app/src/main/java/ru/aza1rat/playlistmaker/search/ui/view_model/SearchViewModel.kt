package ru.aza1rat.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.model.SearchState
import ru.aza1rat.playlistmaker.util.ui.SingleLiveEvent
import ru.aza1rat.playlistmaker.util.ui.debounce

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {
    var searchValue = ""
        private set
    var clickOnTrackAllowed = true
        private set
    private val searchDebounce = debounce<String>(REQUEST_DELAY, viewModelScope, true) { query ->
        if (query.isNotEmpty() && query.isNotBlank()) doSearch(query)
    }
    private val allowClickOnTrackDebounce =
        debounce<Boolean>(TRACK_CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            this.clickOnTrackAllowed = true
        }
    private val searchHistoryEvent = SingleLiveEvent<SearchHistoryEvent>()
    fun observeSearchHistoryEvent(): LiveData<SearchHistoryEvent> = searchHistoryEvent
    private val searchState = MutableLiveData<SearchState>()
    fun observeSearchState(): LiveData<SearchState> = searchState
    private val searchHistoryTracks = MutableLiveData<ArrayList<Track>>()
    fun observeSearchHistoryTracks(): LiveData<ArrayList<Track>> = searchHistoryTracks

    init {
        searchHistoryTracks.value = searchHistoryInteractor.get() as ArrayList<Track>
    }

    fun addTrackToSearchHistory(track: Track) {
        searchHistoryInteractor.add(track, object : SearchHistoryRepository.SearchHistoryCallback {
            override fun onTrackInserted(position: Int) {
                searchHistoryEvent.value = SearchHistoryEvent.TrackInserted(position)
            }

            override fun onTrackRemoved(position: Int) {
                searchHistoryEvent.value = SearchHistoryEvent.TrackRemoved(position)
            }
        })
    }

    fun clearSearchHistory() {
        searchHistoryEvent.value = SearchHistoryEvent.TracksCleared(searchHistoryInteractor.clear())
        searchState.value = SearchState.Idle
    }

    fun doSearch(query: String) {
        searchState.value = SearchState.Loading
        viewModelScope.launch {
            trackInteractor.searchTracks(query).collect { tracks ->
                if (tracks == null) searchState.postValue(SearchState.Error)
                else {
                    if (tracks.isNotEmpty()) {
                        searchState.postValue(SearchState.TracksContent(tracks))
                    } else searchState.postValue(SearchState.Empty)
                }
            }
        }
    }

    fun onTextChanged(query: String, textHasFocus: Boolean, searchHistoryVisible: Boolean) {
        if (searchValue == query) return
        searchValue = query
        if (query.isEmpty() && textHasFocus && searchHistoryInteractor.get().isNotEmpty())
            searchState.value = SearchState.SearchHistory
        if (searchHistoryVisible && query.isNotEmpty()) searchState.value = SearchState.Idle
        searchDebounce.invoke(query)
    }

    fun trackClickDebounce() {
        clickOnTrackAllowed = false
        allowClickOnTrackDebounce.invoke(true)
    }

    sealed interface SearchHistoryEvent {
        data class TrackInserted(val position: Int) : SearchHistoryEvent
        data class TrackRemoved(val position: Int) : SearchHistoryEvent
        data class TracksCleared(val count: Int) : SearchHistoryEvent
    }

    companion object {
        private const val REQUEST_DELAY = 2000L
        private const val TRACK_CLICK_DEBOUNCE_DELAY = 1000L
    }
}