package ru.aza1rat.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.aza1rat.playlistmaker.creator.Creator
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryInteractor
import ru.aza1rat.playlistmaker.history.domain.api.SearchHistoryRepository
import ru.aza1rat.playlistmaker.search.domain.api.TrackInteractor
import ru.aza1rat.playlistmaker.search.domain.model.Track
import ru.aza1rat.playlistmaker.search.ui.model.SearchState
import ru.aza1rat.playlistmaker.util.ui.SingleLiveEvent

class SearchViewModel (
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel()
{
    var searchValue = ""
        private set
    private val handler = Handler(Looper.getMainLooper())
    private val sendRequestTask = Runnable {
            if (searchValue.isNotEmpty() && searchValue.isNotBlank()) {
                doSearch(searchValue)
            }
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

    override fun onCleared() {
        handler.removeCallbacks(sendRequestTask)
        super.onCleared()
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
        trackInteractor.searchTracks(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?) {
                if (foundTracks == null) {
                    searchState.postValue(SearchState.Error)
                }
                else {
                    if (foundTracks.isNotEmpty()) {
                        searchState.postValue(SearchState.TracksContent(foundTracks))
                    }
                    else
                        searchState.postValue(SearchState.Empty)
                }
            }

        })
    }
    fun onTextChanged(query: String, textHasFocus: Boolean, searchHistoryVisible: Boolean) {
        if (searchValue == query) return
        searchValue = query
        if (query.isEmpty() && textHasFocus && searchHistoryInteractor.get().isNotEmpty())
            searchState.value = SearchState.SearchHistory
        if (searchHistoryVisible && query.isNotEmpty())
            searchState.value = SearchState.Idle
        handler.removeCallbacks(sendRequestTask)
        handler.postDelayed(sendRequestTask, REQUEST_DELAY)
    }

    sealed interface SearchHistoryEvent {
        data class TrackInserted(val position: Int) : SearchHistoryEvent
        data class TrackRemoved(val position: Int) : SearchHistoryEvent
        data class TracksCleared(val count: Int): SearchHistoryEvent
    }

    companion object {
        private const val REQUEST_DELAY = 2000L
        fun getFactory() : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideTrackInteractor(),
                    Creator.provideSearchHistoryInteractor()
                )
            }
        }
    }
}