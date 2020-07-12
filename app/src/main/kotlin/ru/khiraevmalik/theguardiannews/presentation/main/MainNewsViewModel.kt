package ru.khiraevmalik.theguardiannews.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import ru.khiraevmalik.theguardiannews.CoroutinesViewModel
import ru.khiraevmalik.theguardiannews.Result
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.mappers.NewsMapper
import ru.khiraevmalik.theguardiannews.mappers.map

class MainNewsViewModel(
        private val newsInteractor: NewsInteractor
) : CoroutinesViewModel() {

    private val mNews = MutableLiveData<Result<List<NewsItem>>>()
    private val mSearchResult = MutableLiveData<Result<List<NewsItem>>>()
    private val mSearchState = MutableLiveData<SearchState>()
    val news: LiveData<Result<List<NewsItem>>> = mNews
    val searchState: LiveData<SearchState> = mSearchState

    private val searchQuery = Channel<String>(Channel.CONFLATED)

    companion object {
        private const val SEARCH_QUERY_DEBOUNCE = 500L
    }

    init {
        launch(Dispatchers.Default) {
            searchQuery.receiveAsFlow()
                    .distinctUntilChanged()
                    .collectLatest { query ->
                        delay(SEARCH_QUERY_DEBOUNCE)
                        handleQuery(query)
                    }
        }
    }

    fun searchQuery(query: String) {
        searchQuery.offer(query)
    }

    fun requestLastNewsResult() {
        mNews.value = mNews.value
    }

    fun requestLastSearchResult() {
        mSearchResult.value = mSearchResult.value
    }

    fun loadNews() {
        mNews.postValue(Result.Loading(mNews.value?.data))
        launch {
            newsInteractor
                    .loadNews()
                    .map(NewsMapper::mapToNewsItems, mNews.value?.data)
                    .let(mNews::postValue)
        }
    }

    private suspend fun handleQuery(query: String) {
        if (query.isEmpty()) {
            mSearchState.postValue(SearchState.EmptyQuery)
        } else {
            mSearchState.postValue(SearchState.Loading)
            mSearchResult.postValue(Result.Loading(mSearchResult.value?.data))
            val res = newsInteractor.search(query)
                    .map(NewsMapper::mapToNewsItems, mSearchResult.value?.data)

            mSearchResult.postValue(res)
            when (res) {
                is Result.Success -> mSearchState.postValue(if (res.data.isEmpty()) SearchState.NotFound else SearchState.Success(res.data))
                is Result.Error -> mSearchState.postValue(SearchState.Error)
            }
        }
    }

    sealed class SearchState {
        object EmptyQuery : SearchState()
        object NotFound : SearchState()
        object Loading : SearchState()
        object Error : SearchState()
        class Success(val news: List<NewsItem>) : SearchState()
    }

}
